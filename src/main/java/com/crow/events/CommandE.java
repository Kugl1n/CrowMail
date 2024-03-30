package com.crow.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {


        if (sender instanceof Player) {
            Player player = (Player) sender;

            switch (label.toLowerCase()) {

                case "carta":

                    if (player.getInventory().firstEmpty() == -1) {
                        player.sendMessage("§8§l[Crow§f§lMail§8§l] §r§7Seu inventario está cheio.");
                    } else {
                        // Sends Message
                        player.sendMessage("§8§l[Crow§f§lMail§8§l] §r§7Carta Criada.");

                        // Creates and Gives the Book to Player
                        player.getInventory().addItem(LetterCreator.createLetter(player, args, false));

                    }

                    break;

                // Comando pra checar quem escreveu a carta - Super
                case "infocarta":
                    if (args.length == 0){
                        if (LetterChecker.isHoldingLetter(player)){
                            player.sendMessage("Esta carta foi escrita por: " + ChatColor.GREEN + LetterCreator.getLetterOwner(player));
                        } else {
                            player.sendMessage(ChatColor.RED + "Você não está segurando uma carta!");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "Usagem incorreta! Utilize somente /infocarta");
                    }
                    break;

                case "cartaanonima":
                    if (player.getInventory().firstEmpty() == -1) {
                        player.sendMessage("§8§l[Crow§f§lMail§8§l] §r§7Seu inventario está cheio.");
                    } else {
                        // Sends Message
                        player.sendMessage("§8§l[Crow§f§lMail§8§l] §r§7Carta Criada.");

                        // Creates and Gives the Book to Player
                        player.getInventory().addItem(LetterCreator.createLetter(player, args, true));
                    }

                    break;

                case "enviar":

                    // Verify if The Destination is not null
                    if (args.length == 0) {
                        player.sendMessage("§8§l[Crow§f§lMail§8§l] §r§7Adicione um Destinatario.");
                        break;
                    }

                    ItemStack letter = new ItemStack(player.getInventory().getItemInMainHand());

                    if (LetterChecker.isHoldingLetter(player)) {

                        if (LetterChecker.wasSent(letter)) {
                            player.sendMessage("§8§l[Crow§f§lMail§8§l] §r§7Carta Invalida.");
                            break;
                        }

                        createOutgoingLetter(args[0], letter);
                        player.getInventory().getItemInMainHand().setAmount(0);

                        player.sendMessage("§8§l[Crow§f§lMail§8§l] §r§7Carta Enviada.");

                        break;
                    }

                    player.sendMessage("§8§l[Crow§f§lMail§8§l] §r§7Você não está segurando uma carta.");
                    break;
            }
        };
        return true;
    }

    @SuppressWarnings("deprecation") 
    public static void createOutgoingLetter(String destinationPlayer, ItemStack letter){

        OfflinePlayer player = Bukkit.getOfflinePlayer(destinationPlayer);
        OutgoingLetter.newOutgoingLetter(player, letter);

    }

}
