package me.timt.smi;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class TraitorShop
  implements CommandExecutor, Listener
{
  @EventHandler
  public void onClose(InventoryCloseEvent e)
  {
    closeInv((Player)e.getPlayer());
  }
  @EventHandler
  public void onKick(PlayerKickEvent e) {
    closeInv(e.getPlayer());
  }
  @EventHandler
  public void onQuit(PlayerQuitEvent e) {
    closeInv(e.getPlayer());
  }
  @EventHandler
  public void onClick(InventoryClickEvent e) {
    if (e.getSlot() == e.getRawSlot()) {
      e.setCancelled(true);
      Player p = (Player)e.getWhoClicked();
      Inventory i = p.getInventory();
      ItemStack is1 = new ItemStack(276, 1);
      p.updateInventory();
      if (e.getSlot() == 16) {
        i.addItem(new ItemStack[] { new ItemStack(359, 1) });
      }
      else if (e.getSlot() == 17)
        i.addItem(new ItemStack[] { new ItemStack(7, 1) });
    }
  }

  public boolean onCommand(CommandSender cs, Command arg1, String arg2, String[] arg3)
  {
    if (!(cs instanceof Player)) {
      cs.sendMessage(ChatColor.AQUA + "[MineFection] " + ChatColor.GOLD + "Access denied!");
      return true;
    }
    Player p = (Player)cs;
    openInv(p);

    return false;
  }
  public void openInv(Player p) {
    Inventory inv = Bukkit.createInventory(null, 18, "Infected Shop");
    ItemStack Item1 = new ItemStack(Material.GHAST_TEAR);
    inv.setItem(0, Item1);
    ItemStack Item2 = new ItemStack(Material.MONSTER_EGG);
    inv.setItem(1, Item2);

    p.openInventory(inv);
  }

  public void closeInv(Player p) {
    p.closeInventory();
  }
}