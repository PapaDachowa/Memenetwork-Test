package net.memenetwork.memenetworktest;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;

@Plugin(
        id = "memenetwork-test",
        name = "Memenetwork Test",
        description = "Too lazy to put a description.",
        url = "https://memenetwork.net",
        authors = {
                "PapaDachowa"
        }
)
public class MemenetworkTest {

    @Inject
    private Logger logger;

    private ArrayList<BlockState> blockList = new ArrayList<>();


    @Listener
    public void onServerStart(GameInitializationEvent event) {
        CommandSpec cmd = CommandSpec.builder()
                .arguments(
                        GenericArguments.string(Text.of("block"))
                )
                .permission("memenetwork.add")
                .executor((source, args) -> {
                    if (source instanceof Player) {
                        BlockState block = args.<BlockState>getOne("block").get();
                        blockList.add(block);
                        source.sendMessage(Text.of(block + " has been added!"));
                    }
                    return CommandResult.success();
                })
                .build();

        Sponge.getCommandManager().register(this, cmd, "add");
    }

    @Listener
    public void onBlockPlace(ChangeBlockEvent.Place event, @Root Player player) {
        BlockSnapshot targetBlock = event.getTransactions().get(0).getFinal();

        if (blockList.contains(targetBlock)) {
            event.setCancelled(true);
            player.sendMessage(Text.of("You are not allowed to place " + targetBlock));
        }
    }

}
