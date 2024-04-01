package com.crow.events;

import com.crow.config.MainConfig;
import com.crow.config.MessageManager;
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

                    if (player.getInventory().firstEmpty() == -1) {
                        player.sendMessage(MessageManager.INVENTORY_FULL);
                    } else {
                        // Sends Message
                        player.sendMessage(MessageManager.LETTER_CREATED);

                        // Creates and Gives the Book to Player
                        player.getInventory().addItem(LetterCreator.createLetter(player, args, false));

                    }

                    break;

                // Comando pra checar quem escreveu a carta - Super
                case "infocarta":
                    if (args.length == 0){
                        if (LetterChecker.isHoldingLetter(player)){
                            player.sendMessage(MessageManager.WRITTEN_BY + LetterCreator.getLetterOwner(player));
                        } else {
                            player.sendMessage(MessageManager.NOT_HOLDING_LETTER);
                        }
                    } else {
                        player.sendMessage(MessageManager.INCORRECT_USAGE);
                    }
                    break;

                case "cartaanonima":
                    if (player.getInventory().firstEmpty() == -1) {
                        player.sendMessage(MessageManager.INVENTORY_FULL);
                    } else {
                        // Sends Message
                        player.sendMessage(MessageManager.LETTER_CREATED);

                        // Creates and Gives the Book to Player
                        player.getInventory().addItem(LetterCreator.createLetter(player, args, true));
                    }

                    break;

                case "enviar":

                    // Verify if The Destination is not null
                    if (args.length == 0) {
                        player.sendMessage(MessageManager.ADD_RECIEVER);
                        break;
                    }

                    ItemStack letter = new ItemStack(player.getInventory().getItemInMainHand());

                    if (LetterChecker.isHoldingLetter(player)) {

                        if (LetterChecker.wasSent(letter)) {
                            player.sendMessage(MessageManager.INVALID_LETTER);
                            break;
                        }

                        OfflinePlayer reciever = Bukkit.getOfflinePlayer(args[0]);

                        if (reciever.isOnline() && ( player.getWorld() == ((Player) reciever).getWorld())) {

                            double distance = player.getLocation().distance(((Player) reciever).getLocation());
                            new OutgoingLetter(player, letter, distance);
                        }

                        else
                            new OutgoingLetter(player, letter, MainConfig.DIFFERENT_DIMENSION_DELAY);


                        player.getInventory().getItemInMainHand().setAmount(0);
                        player.sendMessage(MessageManager.LETTER_SENT);

                        break;
                    }

                    player.sendMessage(MessageManager.NOT_HOLDING_LETTER);
                    break;

                case "rasgar":
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
                        player.sendMessage(MessageManager.SHRED_LETTER);
                    }

                    break;

                case "bloquearcartas":

                    if (OutgoingLetter.blockedPlayers.contains(player)){
                        player.sendMessage(MessageManager.ENABLE_LETTERS);
                        OutgoingLetter.blockedPlayers.remove(player);
                        
                        OutgoingLetter outgoingLetter = OutgoingLetter.isPlayerIn(player);
                        if (outgoingLetter != null) {
                            outgoingLetter.firstSend();
                        }

                    }
                    else {
                        player.sendMessage(MessageManager.DISABLE_LETTERS);
                        OutgoingLetter.blockedPlayers.add(player);
                    }

                    break;

            }
        };
        return true;
    }

}