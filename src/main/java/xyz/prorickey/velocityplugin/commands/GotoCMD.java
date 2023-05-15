package xyz.prorickey.velocityplugin.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.VelocityBrigadierMessage;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import xyz.prorickey.velocityplugin.Utils;

public class GotoCMD {

    public static BrigadierCommand createBrgadierCommand(final ProxyServer proxy) {
        return new BrigadierCommand(LiteralArgumentBuilder
                .<CommandSource>literal("goto")
                .requires(source -> source.hasPermission("mod.goto"))
                .executes(con -> {
                    if(!(con.getSource() instanceof Player player)) {
                        con.getSource().sendMessage(Utils.cmdMsg("<red>You cannot execute this command from console"));
                        return Command.SINGLE_SUCCESS;
                    }

                    StringBuilder servers = proxy.getAllServers().stream()
                            .map(RegisteredServer::getServerInfo)
                            .map(ServerInfo::getName)
                            .toList()
                            .parallelStream()
                            .collect(StringBuilder::new, StringBuilder::append, (a, b) -> a.append(", ").append(b));

                    player.sendMessage(Utils.cmdMsg("<green>The available servers to go to are: <yellow>" + servers));
                    return Command.SINGLE_SUCCESS;
                })
                .then(RequiredArgumentBuilder.<CommandSource, String>argument("serverName", StringArgumentType.word())
                        .suggests((ctx, builder) -> {
                            proxy.getAllServers().stream()
                                    .map(RegisteredServer::getServerInfo)
                                    .map(ServerInfo::getName)
                                    .toList().forEach(name ->
                                            builder.suggest(name,
                                                    VelocityBrigadierMessage.tooltip(
                                                            Utils.format("<light_purple>" + name)
                                                    )
                                            ));
                            return builder.buildFuture();
                        })
                        .executes(con -> {
                            if(!(con.getSource() instanceof Player player)) {
                                con.getSource().sendMessage(Utils.cmdMsg("<red>You cannot execute this command from console"));
                                return Command.SINGLE_SUCCESS;
                            }

                            String srvName = con.getArgument("serverName", String.class);
                            if(proxy.getServer(srvName).isEmpty()) {
                                player.sendMessage(Utils.cmdMsg("<red>That server does not exist"));
                                return Command.SINGLE_SUCCESS;
                            }

                            player.sendMessage(Utils.cmdMsg("<green>Teleporting you to <yellow>" + srvName));
                            player.createConnectionRequest(proxy.getServer(srvName).get());
                            return Command.SINGLE_SUCCESS;
                        })
                )
                .build());
    }

}
