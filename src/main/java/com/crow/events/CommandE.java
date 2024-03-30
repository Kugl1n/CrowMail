package com.crow.events;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.crow.config.ConfigLoader;
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

                    if (player.getInventory().firstEmpty() == -1) {
                        player.sendMessage(ConfigLoader.MESSAGE_PLUGIN_PREFIX + ConfigLoader.MESSAGE_INVENTORY_FULL);
                    } else {
                        // Sends Message
                        player.sendMessage(ConfigLoader.MESSAGE_PLUGIN_PREFIX + ConfigLoader.MESSAGE_LETTER_CREATED);

                        // Creates and Gives the Book to Player
                        player.getInventory().addItem(LetterCreator.createLetter(player, args, false));

                    }

                    break;

                // Comando pra checar quem escreveu a carta - Super
                case "infocarta":
                    if (args.length == 0){
                        if (LetterChecker.isHoldingLetter(player)){
                            player.sendMessage(ConfigLoader.MESSAGE_PLUGIN_PREFIX + ConfigLoader.MESSAGE_WRITTEN_BY + LetterCreator.getLetterOwner(player));
                        } else {
                            player.sendMessage(ConfigLoader.MESSAGE_PLUGIN_PREFIX + ConfigLoader.MESSAGE_DONT_HOLDING_LETTER);
                        }
                    } else {
                        player.sendMessage(ConfigLoader.MESSAGE_PLUGIN_PREFIX + ConfigLoader.MESSAGE_INCORRECT_USAGE);
                    }
                    break;

                case "cartaanonima":
                    if (player.getInventory().firstEmpty() == -1) {
                        player.sendMessage(ConfigLoader.MESSAGE_PLUGIN_PREFIX + ConfigLoader.MESSAGE_INVENTORY_FULL);
                    } else {
                        // Sends Message
                        player.sendMessage(ConfigLoader.MESSAGE_PLUGIN_PREFIX + ConfigLoader.MESSAGE_LETTER_CREATED);

                        // Creates and Gives the Book to Player
                        player.getInventory().addItem(LetterCreator.createLetter(player, args, true));
                    }

                    break;

                case "enviar":

                    // Verify if The Destination is not null
                    if (args.length == 0) {
                        player.sendMessage(ConfigLoader.MESSAGE_PLUGIN_PREFIX + ConfigLoader.MESSAGE_ADD_RECIEVER);
                        break;
                    }

                    ItemStack letter = new ItemStack(player.getInventory().getItemInMainHand());

                    if (LetterChecker.isHoldingLetter(player)) {

                        if (LetterChecker.wasSent(letter)) {
                            player.sendMessage(ConfigLoader.MESSAGE_PLUGIN_PREFIX + ConfigLoader.MESSAGE_INVALID_LETTER);
                            break;
                        }


                        OfflinePlayer reciever = Bukkit.getOfflinePlayer(args[0]);

                        if (reciever.isOnline() && ( player.getWorld() == ((Player) reciever).getWorld())) {
                            double distance = player.getLocation().distance(((Player) reciever).getLocation());
                            OutgoingLetter.newOutgoingLetter(reciever, letter, distance);
                        }
                        else {
                            OutgoingLetter.newOutgoingLetter(reciever, letter, ConfigLoader.DIFFERENT_DIMENSION_DELAY);
                        }
                        
                        player.getInventory().getItemInMainHand().setAmount(0);
                        player.sendMessage(ConfigLoader.MESSAGE_PLUGIN_PREFIX + ConfigLoader.MESSAGE_LETTER_SEND);

                        break;
                    }

                    player.sendMessage(ConfigLoader.MESSAGE_PLUGIN_PREFIX + ConfigLoader.MESSAGE_DONT_HOLDING_LETTER);
                    break;

                case "bloquearcartas":


                    if (OutgoingLetter.bloquedPlayers.contains(player)){
                        player.sendMessage(ConfigLoader.MESSAGE_PLUGIN_PREFIX + ConfigLoader.MESSAGE_ENABLE_LETTERS);
                        OutgoingLetter.bloquedPlayers.remove(player);

                        if (OutgoingLetter.outgoingLetters.get(player.getUniqueId()).size() > 0) {
                            OutgoingLetter.send(player, false, ConfigLoader.ON_ENABLE_LETTERS_DELAY);
                        }

                    }
                    else {
                        player.sendMessage(ConfigLoader.MESSAGE_PLUGIN_PREFIX + ConfigLoader.MESSAGE_DISABLE_LETTERS);
                        OutgoingLetter.bloquedPlayers.add(player);
                    }
                
                    break;

            }
        };
        return true;
    }

}
