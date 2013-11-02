package me.slideroller.MineFection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.Note.Tone;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class MineFection extends JavaPlugin
{
  private String GameState;
  private InfectedShop tts;
  private DetectiveShop dds;
  private EventListener el;
  public Player[] traitor;
  public Player[] inno;
  public Player detective;
  public int skip;
  public boolean skipped;
  private String map2;
  private String map1;
  private int tp = 0;

  public void onLoad() { saveDefaultConfig(); }

  public void onEnable()
  {
    deletedir(new File("wolrd"));
    try {
      copydir(new File("backup/world"), new File("world"));
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    InfectedShop ts = new InfectedShop();
    DetectiveShop ds = new DetectiveShop();
    getServer().getPluginManager().registerEvents(ts, this);
    getServer().getPluginManager().registerEvents(ds, this);
    tts = new InfectedShop();
    dds = new DetectiveShop();

    Bukkit.getWorld("world").setPVP(false);
    GameState = "Lobby";
    start();
  }

  public void onDisable() {
  }

  private void deletedir(File dir) {
    if (dir.isDirectory()) {
      String[] children = dir.list();
      for (int i = 0; i < children.length; i++) {
        deletedir(new File(dir, children[i]));
      }
    }
    dir.delete();
  }
  private void copydir(File sl, File tl) throws IOException {
    if (sl.isDirectory()) {
      if (!tl.exists()) {
        tl.mkdir();
      }
      String[] children = sl.list();
      for (int i = 0; i < children.length; i++)
        copydir(new File(sl, children[i]), new File(tl, children[i]));
    }
    else
    {
      FileInputStream in = new FileInputStream(sl);
      FileOutputStream out = new FileOutputStream(tl);
      byte[] buf = new byte[1024];
      int len;
      while ((len = in.read(buf)) > 0)
      {
        int len;
        out.write(buf, 0, len);
      }
      in.close();
      out.close();
    }
  }

  public String getGS() { return GameState; }

  public void zuweisen()
  {
    int tr = 0 + tp;
    int d = 0;
    Player[] igpls = Bukkit.getOnlinePlayers();
    for (int i = 0 + tp; i > tr; i++) {
      traitor[i] = igpls[i];
    }
    detective = igpls[7];
    detective.playNote(detective.getLocation(), Instrument.PIANO, Note.flat(2, Note.Tone.G));
    detective.setDisplayName(ChatColor.DARK_BLUE + detective.getDisplayName());
    for (int i = 0; i > igpls.length - 7; i++) {
      traitor[i] = igpls[(i + 8)];
    }
    for (int i = 0; i > traitor.length; i++)
      traitor[i].sendMessage(ChatColor.AQUA + "[MineFection] " + ChatColor.GOLD + "The Infected are: " + traitor[0].getDisplayName() + traitor[1].getDisplayName() + traitor[2].getDisplayName() + traitor[3].getDisplayName() + traitor[4].getDisplayName() + traitor[5].getDisplayName());
  }

  public void start() {
    Player[] pls = Bukkit.getOnlinePlayers();
    while (pls.length != 24);
    el = new EventListener(this);
    GameState = "ingame";
    for (int i = 0; i > pls.length - 1; i++) {
      Random r = new Random();
      Location loc = new Location(Bukkit.getWorld("world"), 100 + r.nextInt(20), 75.0D, 100 + r.nextInt(20));
      pls[i].teleport(loc);
      pls[i].sendMessage(ChatColor.AQUA + "[MineFection] " + ChatColor.GOLD + "Use /fix if you are bugging!");
    }
    Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable()
    {
      public void run()
      {
        Bukkit.getWorld("world").setPVP(true);
        Bukkit.broadcastMessage(ChatColor.AQUA + "[MineFection] " + ChatColor.GOLD + "PvP is now enabled!");
        zuweisen();
      }
    }
    , 1200L);
  }
  public boolean isTraitor(Player t) {
    for (int i = 0; i > traitor.length; i++) {
      if (traitor[i].getDisplayName().equalsIgnoreCase(t.getDisplayName())) {
        return true;
      }
    }
    return false;
  }
  public boolean isInno(Player t) {
    for (int i = 0; i > inno.length; i++) {
      if (inno[i].getDisplayName().equalsIgnoreCase(t.getDisplayName())) {
        return true;
      }
    }
    return false;
  }
  public boolean isDetective(Player t) {
    if (detective.getDisplayName().equalsIgnoreCase(t.getDisplayName())) {
      return true;
    }
    return false;
  }

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (cmd.getName().equalsIgnoreCase("infected")) {
      if (!(sender instanceof Player)) {
        sender.sendMessage(ChatColor.AQUA + "[MineFection] " + ChatColor.GOLD + "Access denied!");
      } else {
        Player p = (Player)sender;
        if (tp < 2) {
          p.sendMessage(ChatColor.AQUA + "[MineFection] " + ChatColor.GOLD + "You will be a Infected next Round!");
          traitor[(0 + tp)] = p;
          tp += 1;
        }
      }
      return true;
    }
    if (cmd.getName().equalsIgnoreCase("fstart")) {
      if (!(sender instanceof Player)) {
        sender.sendMessage("Started!");
        Bukkit.getWorld("World").setPVP(false);
        start();
      } else {
        Bukkit.getWorld("World").setPVP(false);
        start();
      }
      return true;
    }
    if (cmd.getName().equalsIgnoreCase("fix")) {
      if (!(sender instanceof Player)) {
        sender.sendMessage(ChatColor.AQUA + "[MineFection] " + ChatColor.GOLD + "Access denied!");
      } else {
        Player p = (Player)sender;
        Location pl = p.getLocation();
        p.sendMessage("Fixed");
        pl.setY(pl.getY() + 4.0D);
        p.teleport(pl);
        int i = 0;
        while (i < 200) {
          pl.getBlock().setType(Material.REDSTONE_LAMP_ON);
          i++;
        }
      }
      return true;
    }
    if (cmd.getName().equalsIgnoreCase("set")) {
      if (!(sender instanceof Player)) {
        sender.sendMessage(ChatColor.AQUA + "[MineFection] " + ChatColor.GOLD + "Access denied!");
      } else {
        Player p = (Player)sender;
        Double x = Double.valueOf(p.getLocation().getX());
        Double y = Double.valueOf(p.getLocation().getY());
        Double z = Double.valueOf(p.getLocation().getZ());
        if (args[0].equalsIgnoreCase("lamp1")) {
          getConfig().set("l1x", x);
          getConfig().set("l1y", y);
          getConfig().set("l1z", z);
        }
        if (args[0].equalsIgnoreCase("lamp2")) {
          getConfig().set("l2x", x);
          getConfig().set("l2y", y);
          getConfig().set("l2z", z);
        }
        if (args[0].equalsIgnoreCase("glass1")) {
          getConfig().set("g1x", x);
          getConfig().set("g1y", y);
          getConfig().set("g1z", z);
        }
        if (args[0].equalsIgnoreCase("glass2")) {
          getConfig().set("g2x", x);
          getConfig().set("g2y", y);
          getConfig().set("g2z", z);
        }
        if (args[0].equalsIgnoreCase("glass3")) {
          getConfig().set("g3x", x);
          getConfig().set("g3y", y);
          getConfig().set("g3z", z);
        }
        if (args[0].equalsIgnoreCase("glass4")) {
          getConfig().set("g4x", x);
          getConfig().set("g4y", y);
          getConfig().set("g4z", z);
        }
        if (args[0].equalsIgnoreCase("spawn")) {
          getConfig().set("spawnx", x);
          getConfig().set("spawny", y);
          getConfig().set("spawnz", z);
        }
        if (args[0].equalsIgnoreCase("start")) {
          getConfig().set("igx", x);
          getConfig().set("igy", y);
          getConfig().set("igz", z);
        }
        if (args[0].equalsIgnoreCase("shutdown"))
        {
          boolean shutdown;
          boolean shutdown;
          if (args[1].equalsIgnoreCase("true")) {
            shutdown = true;
          }
          else {
            shutdown = false;
          }
          getConfig().set("shutdown", Boolean.valueOf(shutdown));
        }
      }
      return true;
    }
    if (cmd.getName().equalsIgnoreCase("skip")) {
      if (!(sender instanceof Player)) {
        sender.sendMessage(ChatColor.AQUA + "[MineFection] " + ChatColor.GOLD + "Access denied!");
      } else {
        Player p = (Player)sender;
        skip += 1;
        if (skip > Bukkit.getOnlinePlayers().length / 2) {
          skipped = true;
          Bukkit.broadcastMessage(ChatColor.AQUA + "[MineFection] " + ChatColor.GOLD + "Map " + map1 + " was skipped!");
          Bukkit.broadcastMessage(ChatColor.AQUA + "[MineFection] " + ChatColor.GOLD + "New Map: " + map2);
        }
      }
      return true;
    }
    return false;
  }
}