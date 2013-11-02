package me.timt.smi;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;

public class EventListener
  implements Listener
{
  public boolean fixed = false;
  private TIMT p;
  private int dead = 0;
  private int tdead = 0;

  public EventListener(TIMT p) { this.p = p; }

  @EventHandler
  public void onPlayerDeath(PlayerDeathEvent e) {
    e.setDroppedExp(0);
    if (!p.detective.isOnline()) {
      for (int i = 0; i > p.inno.length; i++) {
        if (!p.inno[i].isOnline()) {
          dead += 1;
        }
      }
      if (dead == p.inno.length) {
        Bukkit.broadcastMessage("The Infected have won!");
        shutdown();
      }
    }
    for (int i = 0; i > p.traitor.length; i++) {
      if (!p.traitor[i].isOnline()) {
        tdead += 1;
      }
    }
    if (tdead == p.traitor.length) {
      Bukkit.broadcastMessage("The Humans have won!");
      shutdown();
    }
  }

  private void shutdown() { if (p.getConfig().getBoolean("shutdown")) {
      Bukkit.shutdown();
    }
    else
      Bukkit.getPluginManager().disablePlugin(p);
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent e)
  {
    if (p.isTraitor(e.getPlayer())) {
      tdead += 1;
      e.getPlayer().getInventory().clear();
    }
    else {
      dead += 1;
      e.getPlayer().getInventory().clear();
    }
  }

  @EventHandler
  public void onPlayerJoin(PlayerLoginEvent e) { if (p.getGS().equalsIgnoreCase("Ingame")) {
      e.getPlayer().kickPlayer("Server is Ingame!");
    }
    else
      e.getPlayer().teleport(new Location(Bukkit.getWorld("world"), p.getConfig().getDouble("spawnx"), p.getConfig().getDouble("spawny"), p.getConfig().getDouble("spawnz"))); }

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent e)
  {
    if ((e.getItem() != new ItemStack(271)) || 
      (e.getMaterial() == Material.CHEST)) {
      e.getClickedBlock().setType(Material.AIR);
      Player p = e.getPlayer();
      Inventory i = p.getInventory();
      if ((i.contains(Material.STONE_SWORD)) && (i.contains(Material.BOW)) && (i.contains(Material.WOOD_SWORD))) {
        i.addItem(new ItemStack[] { new ItemStack(Material.ARROW, 64) });
      }
      else if ((i.contains(Material.ARROW)) && (i.contains(Material.BOW)) && (i.contains(Material.WOOD_SWORD))) {
        i.addItem(new ItemStack[] { new ItemStack(Material.STONE_SWORD) });
      }
      else if ((i.contains(Material.STONE_SWORD)) && (i.contains(Material.ARROW)) && (i.contains(Material.WOOD_SWORD))) {
        i.addItem(new ItemStack[] { new ItemStack(Material.BOW) });
      }
      else if ((i.contains(Material.STONE_SWORD)) && (i.contains(Material.BOW)) && (i.contains(Material.ARROW))) {
        i.addItem(new ItemStack[] { new ItemStack(Material.WOOD_SWORD) });
      }
    }
    if (e.getMaterial() == Material.ENDER_CHEST) {
      e.getClickedBlock().setType(Material.AIR);
      Player p = e.getPlayer();
      Inventory i = p.getInventory();
      i.addItem(new ItemStack[] { new ItemStack(Material.IRON_SWORD) });
    }
    if (!this.p.isTraitor(e.getPlayer())) {
      Location glass1 = new Location(e.getPlayer().getWorld(), this.p.getConfig().getDouble("g1x"), this.p.getConfig().getDouble("g1y"), this.p.getConfig().getDouble("g1z"));
      Location glass2 = new Location(e.getPlayer().getWorld(), this.p.getConfig().getDouble("g2x"), this.p.getConfig().getDouble("g2y"), this.p.getConfig().getDouble("g2z"));
      Location glass3 = new Location(e.getPlayer().getWorld(), this.p.getConfig().getDouble("g3x"), this.p.getConfig().getDouble("g3y"), this.p.getConfig().getDouble("g3z"));
      Location glass4 = new Location(e.getPlayer().getWorld(), this.p.getConfig().getDouble("g4x"), this.p.getConfig().getDouble("g4y"), this.p.getConfig().getDouble("g4z"));
      glass1.getBlock().setType(Material.GLASS);
      glass2.getBlock().setType(Material.GLASS);
      glass3.getBlock().setType(Material.GLASS);
      glass4.getBlock().setType(Material.GLASS);
      Bukkit.getScheduler().scheduleSyncDelayedTask(this.p, new Runnable()
      {
        public void run()
        {
          Location glass1 = new Location(Bukkit.getWorld("world"), p.getConfig().getDouble("g1x"), p.getConfig().getDouble("g1y"), p.getConfig().getDouble("g1z"));
          Location glass2 = new Location(Bukkit.getWorld("world"), p.getConfig().getDouble("g2x"), p.getConfig().getDouble("g2y"), p.getConfig().getDouble("g2z"));
          Location glass3 = new Location(Bukkit.getWorld("world"), p.getConfig().getDouble("g3x"), p.getConfig().getDouble("g3y"), p.getConfig().getDouble("g3z"));
          Location glass4 = new Location(Bukkit.getWorld("world"), p.getConfig().getDouble("g4x"), p.getConfig().getDouble("g4y"), p.getConfig().getDouble("g4z"));
          glass1.getBlock().setType(Material.AIR);
          glass2.getBlock().setType(Material.AIR);
          glass3.getBlock().setType(Material.AIR);
          glass4.getBlock().setType(Material.AIR);
        }
      }
      , 60L);
    }if (this.p.isTraitor(e.getPlayer())) {
      Location lamp1 = new Location(e.getPlayer().getWorld(), this.p.getConfig().getDouble("l1x"), this.p.getConfig().getDouble("l1y"), this.p.getConfig().getDouble("l1z"));
      Location lamp2 = new Location(e.getPlayer().getWorld(), this.p.getConfig().getDouble("l2x"), this.p.getConfig().getDouble("l2y"), this.p.getConfig().getDouble("l2z"));
      lamp1.getBlock().setType(Material.GLOWSTONE);
      lamp2.getBlock().setType(Material.GLOWSTONE);
      Location glass1 = new Location(e.getPlayer().getWorld(), this.p.getConfig().getDouble("g1x"), this.p.getConfig().getDouble("g1y"), this.p.getConfig().getDouble("g1z"));
      Location glass2 = new Location(e.getPlayer().getWorld(), this.p.getConfig().getDouble("g2x"), this.p.getConfig().getDouble("g2y"), this.p.getConfig().getDouble("g2z"));
      Location glass3 = new Location(e.getPlayer().getWorld(), this.p.getConfig().getDouble("g3x"), this.p.getConfig().getDouble("g3y"), this.p.getConfig().getDouble("g3z"));
      Location glass4 = new Location(e.getPlayer().getWorld(), this.p.getConfig().getDouble("g4x"), this.p.getConfig().getDouble("g4y"), this.p.getConfig().getDouble("g4z"));
      glass1.getBlock().setType(Material.AIR);
      glass2.getBlock().setType(Material.AIR);
      glass3.getBlock().setType(Material.AIR);
      glass4.getBlock().setType(Material.AIR);
      lamp1.getBlock().setType(Material.REDSTONE_LAMP_OFF);
      lamp2.getBlock().setType(Material.REDSTONE_LAMP_OFF);
    }
  }
}