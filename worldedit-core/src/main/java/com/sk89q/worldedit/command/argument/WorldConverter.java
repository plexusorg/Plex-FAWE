/*
 * WorldEdit, a Minecraft world manipulation toolkit
 * Copyright (C) sk89q <http://www.sk89q.com>
 * Copyright (C) WorldEdit team and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.sk89q.worldedit.command.argument;

import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.extension.platform.Capability;
import com.sk89q.worldedit.util.formatting.text.Component;
import com.sk89q.worldedit.util.formatting.text.TextComponent;
import com.sk89q.worldedit.world.World;
import org.enginehub.piston.CommandManager;
import org.enginehub.piston.converter.ArgumentConverter;
import org.enginehub.piston.converter.ConversionResult;
import org.enginehub.piston.converter.FailedConversion;
import org.enginehub.piston.converter.SuccessfulConversion;
import org.enginehub.piston.inject.InjectedValueAccess;
import org.enginehub.piston.inject.Key;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WorldConverter implements ArgumentConverter<World> {

    public static void register(CommandManager commandManager) {
        commandManager.registerConverter(Key.of(World.class), WORLD_CONVERTER);
    }

    //FAWE start - Accessed by LocationConverter
    public static final WorldConverter WORLD_CONVERTER = new WorldConverter();
    //FAWE end

    private final TextComponent choices;

    private WorldConverter() {
        this.choices = TextComponent.of("any world");
    }

    @Override
    public Component describeAcceptableArguments() {
        return this.choices;
    }

    private Stream<? extends World> getWorlds() {
        return WorldEdit.getInstance().getPlatformManager()
                .queryCapability(Capability.GAME_HOOKS).getWorlds().stream();
    }

    @Override
    public List<String> getSuggestions(String input, InjectedValueAccess context) {
        return getWorlds()
                .map(World::id)
                .filter(world -> world.startsWith(input))
                .collect(Collectors.toList());
    }

    @Override
    public ConversionResult<World> convert(String s, InjectedValueAccess injectedValueAccess) {
        World result = getWorlds()
                .filter(world -> world.id().equals(s))
                .findAny().orElse(null);
        return result == null
                ? FailedConversion.from(new IllegalArgumentException(
                "Not a valid world: " + s))
                : SuccessfulConversion.fromSingle(result);
    }

}
