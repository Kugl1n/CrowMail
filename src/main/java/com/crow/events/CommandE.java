package com.crow.events;

import com.crow.config.ConfigLoader;
import com.crow.config.MainConfig;
import com.crow.config.MessageManager;
import com.crow.config.OutgoingManager;

import java.util.ArrayList;

import com.crow.crow.Crow;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.crow.letter.LetterChecker;
import com.crow.letter.LetterCreator;
import com.crow.letter.OutgoingLetter;



public class CommandE implements CommandExecutor{

    @Override
    @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {


        if (sender instanceof Player) {
            Player player = (Player) sender;

            switch (label.toLowerCase()) {

                case "carta":

                    if (player.hasPermission("crowmail.carta")) {
                        if (player.getInventory().firstEmpty() == -1) {
                            player.sendMessage(MessageManager.INVENTORY_FULL);
                            break;
                        } else {
                            // Sends Message
                            player.sendMessage(MessageManager.LETTER_CREATED);

                            // Creates and Gives the Book to Player
                            player.getInventory().addItem(LetterCreator.createLetter(player, args, false));
                        }
                        break;
                    }
                    player.sendMessage(MessageManager.ERROR_NO_PERMISSION);
                    break;

                //Comando que retorna o usuário que escreveu uma carta
                case "infocarta":

                    if (player.hasPermission("crowmail.infocarta")) {
                        if (args.length == 0){
                            if (LetterChecker.isHoldingLetter(player)){
                                player.sendMessage(MessageManager.WRITTEN_BY + LetterCreator.getLetterOwner(player));
                            } else {
                                player.sendMessage(MessageManager.NOT_HOLDING_LETTER);
                            }
                        } else {
                            player.sendMessage(MessageManager.INCORRECT_USAGE + " /infocarta");
                            break;
                        }
                    } else {
                        player.sendMessage(MessageManager.ERROR_NO_PERMISSION);
                    }

                    break;

                case "cartaanonima":
                    if (player.hasPermission("crowmail.cartaanonima")) {
                        if (player.getInventory().firstEmpty() == -1) {
                            player.sendMessage(MessageManager.INVENTORY_FULL);
                        } else {
                            // Sends Message
                            player.sendMessage(MessageManager.LETTER_CREATED);

                            // Creates and Gives the Book to Player
                            player.getInventory().addItem(LetterCreator.createLetter(player, args, true));
                        }
                        break;
                    }

                    player.sendMessage(MessageManager.ERROR_NO_PERMISSION);
                    break;

                case "enviar":

                    if (player.hasPermission("corwmail.enviar")) {
                        if (args.length == 0) {
                            player.sendMessage(MessageManager.ADD_RECIEVER);
                            break;
                        }
                        ItemStack letter = new ItemStack(player.getInventory().getItemInMainHand());
                        if (LetterChecker.isHoldingOwnLetter(player)) {

                            if (LetterChecker.wasSent(letter)) {
                                player.sendMessage(MessageManager.INVALID_LETTER);
                                break;
                            }

                            OfflinePlayer reciever = Bukkit.getOfflinePlayer(args[0]);

                            if (reciever.isOnline() && ( player.getWorld() == ((Player) reciever).getWorld())) {

                                double distance = player.getLocation().distance(((Player) reciever).getLocation());
                                new OutgoingLetter(reciever, letter, distance);
                            }

                            else
                                new OutgoingLetter(reciever, letter, MainConfig.DIFFERENT_DIMENSION_DELAY);

                            player.getInventory().getItemInMainHand().setAmount(0);
                            player.sendMessage(MessageManager.LETTER_SENT);

                            break;
                        }

                        player.sendMessage(MessageManager.NOT_HOLDING_OWN_LETTER);
                        break;
                    }

                    player.sendMessage(MessageManager.ERROR_NO_PERMISSION);
                    break;

                case "rasgar":

                    if (player.hasPermission("crowmail.rasgar")) {
                        if (args.length == 0 ){
                            if (LetterChecker.isHoldingLetter(player)) {
                                player.getInventory().getItemInMainHand().setAmount(0);
                                player.sendMessage(MessageManager.SHRED_LETTER);
                            }
                            else
                                player.sendMessage(MessageManager.NOT_HOLDING_LETTER);
                        }

                        if (args.length > 0 && (args[0].equals("todas") || args[0].equals("tudo")|| args[0].equals("all")) ) {
                            for (ItemStack item : player.getInventory().getContents()) {
                                if (LetterChecker.isValidLetter(item))
                                    item.setAmount(0);
                            }
                            player.sendMessage(MessageManager.SHRED_ALL_LETTERS);
                        }

                        break;
                    }
                    player.sendMessage(MessageManager.ERROR_NO_PERMISSION);
                    break;

                case "bloquearcartas":

                    if (player.hasPermission("crowmail.bloquear")) {
                        if (OutgoingLetter.blockedPlayers.contains(player)){
                            player.sendMessage(MessageManager.ENABLE_LETTERS);

                            OutgoingLetter.blockedPlayers.remove(player);

                            ArrayList<OutgoingLetter> outgoingLetters = OutgoingLetter.isPlayerIn(player);
                            for (OutgoingLetter outgoingLetter : outgoingLetters)
                                outgoingLetter.send(MainConfig.ON_GAMEMODE_DELAY);
                        }
                        else {
                            player.sendMessage(MessageManager.DISABLE_LETTERS);
                            OutgoingLetter.blockedPlayers.add(player);
                        }

                        break;
                    }
                    player.sendMessage(MessageManager.ERROR_NO_PERMISSION);
                    break;

                case "cm":
                case "crowmail":
                    if (args.length == 1){
                        if (args[0].equalsIgnoreCase("reload")) {
                            if (player.hasPermission("crowmail.reload")) {
                                OutgoingManager.saveLetters();
                                Crow.removeAllCrows();

                                ConfigLoader.getOutgoingConfig().reloadConfig();
                                ConfigLoader.getMessageConfig().reloadConfig();
                                ConfigLoader.getMainConfig().reloadConfig();

                                MainConfig.loadConfigs();
                                MessageManager.reloadMessages();
                                OutgoingManager.loadLetters();

                                player.sendMessage(MessageManager.RELOAD_SUCCESS);
                            } else {
                                player.sendMessage(MessageManager.ERROR_NO_PERMISSION);
                            }
                        } else {
                            player.sendMessage(MessageManager.INCORRECT_USAGE + " /crowmail reload");
                        }

                    }
                    break;

            }
        }
        return true;
    }

}