// I added some stuff I didn't have enough time to push it so i went on the url and editited if more needed pm

package me.slideroller.arena;

public class Game {

	public Object getPlayers() {
		// TODO Auto-generated method stub
		return null;
	}
	  public ItemStack rename(ItemStack item, String name)
  {
    ItemMeta im = item.getItemMeta();
    im.setDisplayName(name);
    item.setItemMeta(im);
    return item;
  }

  public ItemStack getDetecStick()
  {
    ItemStack i = new ItemStack(Material.STICK);
    rename(i, ChatColor.GREEN + ChatColor.translateAlternateColorCodes('&', messages.getMessage("Tools.Detectivstick")));
    return i;
  }

  public void startGame(final int arena)
  {
    teleportStart(arena);
    peacePeriod.add(arena);
    preAssign(arena);
    sendArenaMessage(ChatColor.DARK_RED + prefix_p + " " + ChatColor.GOLD + ChatColor.translateAlternateColorCodes('&', messages.getMessage("Arena.Start30")), arena);
    updateSign(arena, ChatColor.DARK_PURPLE + "[InGame]", "");
    getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable()
    {
      public void run()
      {
        assign(arena);
        peacePeriod.remove(arena);
        updateSign(arena, ChatColor.DARK_PURPLE + "[InGame]", "");
        sendArenaMessage(ChatColor.DARK_RED + prefix_p + " " + ChatColor.GOLD + ChatColor.translateAlternateColorCodes('&', messages.getMessage("Arena.EndPeacePeriod")), arena);
      }
    }
    , 600L);
  }

  public void preAssign(int arena)
  {
    for (int i = 0; i < maxPlayers; i++)
    {
      if (players[arena][i] != null)
      {
        int randomNumber = rd.nextInt(3);
        getConfig().set("Players.type." + players[arena][i].getName(), Integer.valueOf(randomNumber));

        saveConfig();
      }
    }
  }

  public void assign(int arena)
  {
    int t = 0;
    int d = 0;

    for (int m = 0; m < maxPlayers; m++)
    {
      if (players[arena][m] != null)
      {
        String pn = players[arena][m].getName();

        if ((getConfig().getInt("Players.type." + pn) == 1) && (t < maxInfected + 1))
        {
          players[arena][m].sendMessage(ChatColor.DARK_RED + prefix_p + " " + ChatColor.GOLD + ChatColor.translateAlternateColorCodes('&', messages.getMessage("Arena.YouInfected")));
          players[arena][m].sendMessage(ChatColor.DARK_RED + prefix_p + " " + ChatColor.GOLD + ChatColor.translateAlternateColorCodes('&', messages.getMessage("Arena.YouInfected2")));
          Infected.put(pn, String.valueOf(arena));
          t++;
        }
        else if ((getConfig().getInt("Players.type." + pn) == 2) && (d < maxscientists + 1))
        {
          d++;
          players[arena][m].sendMessage(ChatColor.DARK_RED + prefix_p + " " + ChatColor.GOLD + ChatColor.translateAlternateColorCodes('&', messages.getMessage("Arena.YouDetective")));
          sendArenaMessage(ChatColor.DARK_RED + prefix_p + " " + ChatColor.GOLD + ChatColor.translateAlternateColorCodes('&', messages.getMessage("Arena.DetectiveAll").replaceAll("%player", players[arena][m].getName())), arena);
          ItemStack item = getDetecStick();
          players[arena][m].getInventory().addItem(new ItemStack[] { item });
          scientists.put(pn, String.valueOf(arena));
        }
        else
        {
          players[arena][m].sendMessage(ChatColor.DARK_RED + prefix_p + " " + ChatColor.GOLD + ChatColor.translateAlternateColorCodes('&', messages.getMessage("Arena.YouInnocent")));
          players[arena][m].sendMessage(ChatColor.DARK_RED + prefix_p + " " + ChatColor.GOLD + ChatColor.translateAlternateColorCodes('&', messages.getMessage("Arena.YouInnocent2")));
          humans.put(pn, String.valueOf(arena));
        }

        if ((!Infected.containsKey(players[arena][m].getName())) && (playersUsedTP.contains(players[arena][m]))) {
          players[arena][m].sendMessage(ChatColor.DARK_RED + prefix_p + " " + ChatColor.GOLD + ChatColor.translateAlternateColorCodes('&', messages.getMessage("Errors.UsedInfectedPassButNoInfected")));
          PlayerConfig.addInfectedPass(players[arena][m], 1);
        }
      }
    }

    for (int x = 0; x < maxPlayers; x++)
    {
      if ((players[arena][x] != null) && (players[arena][x].isOnline()))
      {
        TagAPI.refreshPlayer(players[arena][x]);
      }
    }
  }

  public void sendArenaMessage(String message, int arena)
  {
    for (int i = 0; i < maxPlayers; i++)
    {
      if (players[arena][i] != null)
      {
        players[arena][i].sendMessage(message);
      }
    }
  }

  public void stopGame(int arena)
  {
    resetChest(arena);
    teleportBack(arena);
    removeItems(arena);

    for (int i = 0; i < maxPlayers; i++)
    {
      if (players[arena][i] != null)
      {
        players[arena][i].getInventory().clear();
        playersIngameDead.remove(players[arena][i]);

        if (Infected.containsKey(players[arena][i].getName()))
        {
          Infected.remove(players[arena][i].getName());
        }
        else if (scientists.containsKey(players[arena][i].getName()))
        {
          scientists.remove(players[arena][i].getName());
        }
        else if (humans.containsKey(players[arena][i].getName()))
        {
          humans.remove(players[arena][i].getName());
        }

        players[arena][i].setHealth(20.0D);
        players[arena][i].setGameMode(GameMode.SURVIVAL);
        players[arena][i].setAllowFlight(false);
        playersIngame.remove(players[arena][i]);

        if (players[arena][i].isOnline())
        {
          TagAPI.refreshPlayer(players[arena][i]);

          for (Player onPl : getServer().getOnlinePlayers())
          {
            onPl.showPlayer(players[arena][i]);
          }

          players[arena][i].loadData();
          players[arena][i] = null;
        }
      }
    }
    playersAmount[arena] = 0;
    gameStarted.remove(arena);
    peacePeriod.remove(arena);
    updateSign(arena, ChatColor.DARK_GREEN + "[JOIN]", "");
  }

  public void resetChest(int arena)
  {
    for (int i = 0; i < getConfig().getInt("Arenen." + arena + ".kisten.anzahl"); i++)
    {
      int posX = getConfig().getInt("Arenen." + arena + ".kisten." + i + ".posX");
      int posY = getConfig().getInt("Arenen." + arena + ".kisten." + i + ".posY");
      int posZ = getConfig().getInt("Arenen." + arena + ".kisten." + i + ".posZ");
      String world = getConfig().getString("Arenen." + arena + ".kisten." + i + ".world");
      Location chestLoc = new Location(Bukkit.getWorld(world), posX, posY, posZ);
      Bukkit.getWorld(world).getBlockAt(chestLoc).setType(Material.CHEST);
    }

    for (int i = 0; i < getConfig().getInt("Arenen." + arena + ".enderkisten.anzahl"); i++)
    {
      int posX = getConfig().getInt("Arenen." + arena + ".enderkisten." + i + ".posX");
      int posY = getConfig().getInt("Arenen." + arena + ".enderkisten." + i + ".posY");
      int posZ = getConfig().getInt("Arenen." + arena + ".enderkisten." + i + ".posZ");
      String world = getConfig().getString("Arenen." + arena + ".enderkisten." + i + ".world");
      Location chestLoc = new Location(Bukkit.getWorld(world), posX, posY, posZ);
      Bukkit.getWorld(world).getBlockAt(chestLoc).setType(Material.ENDER_CHEST);
    }

    getConfig().set("Arenen." + arena + ".kisten.anzahl", Integer.valueOf(0));
    getConfig().set("Arenen." + arena + ".enderkisten.anzahl", Integer.valueOf(0));
  }

  public void playerJoinArena(Player p, String[] lines)
  {
    p.saveData();

    boolean eingetragen = false;

    final int arena = Integer.parseInt(lines[1]);
    int karma = PlayerConfig.getKarma(p);

    playersIngame.add(p);
    eingetragen = false;

    for (int i = 0; i < maxPlayers; i++)
    {
      if ((players[arena][i] == null) && (!eingetragen))
      {
        players[arena][playersAmount[arena]] = p;
        playersAmount[arena] += 1;
        eingetragen = true;
      }

    }

    p.setGameMode(GameMode.SURVIVAL);

    p.setHealth(20.0D);
    p.setFoodLevel(20);

    p.getInventory().clear();
    p.getInventory().setBoots(null);
    p.getInventory().setChestplate(null);
    p.getInventory().setHelmet(null);
    p.getInventory().setLeggings(null);
    p.setAllowFlight(false);

    p.setLevel(karma);

    p.updateInventory();
    try
    {
      int posX = getConfig().getInt("Arenen." + lines[1] + ".spawn.lobby.posX");
      int posY = getConfig().getInt("Arenen." + lines[1] + ".spawn.lobby.posY");
      int posZ = getConfig().getInt("Arenen." + lines[1] + ".spawn.lobby.posZ");
      float pitch = getConfig().getInt("Arenen." + lines[1] + ".spawn.lobby.pitch");
      float yaw = getConfig().getInt("Arenen." + lines[1] + ".spawn.lobby.yaw");
      String world = getConfig().getString("Arenen." + lines[1] + ".spawn.lobby.world");
      Location teleportloc = new Location(Bukkit.getWorld(world), posX, posY, posZ, pitch, yaw);
      p.teleport(teleportloc);
    }
    catch (NullPointerException e)
    {
      p.sendMessage(ChatColor.RED + prefix_p + "[WARNUNG] An error ocurred. Please contact serveradministration!");
      System.out.println(prefix_c + "[WARNUNG] " + p.getName() + " couldn't teleport to arena!");
      return;
    }
    String world;
    float yaw;
    float pitch;
    int posZ;
    int posY;
    int posX;
    int anzahl = playersAmount[arena];
    String message = ChatColor.translateAlternateColorCodes('&', messages.getMessage("Arena.Join").replaceAll("%player", p.getName()));
    message = message.replaceAll("%plAmount", String.valueOf(anzahl));
    message = message.replaceAll("%maxPl", String.valueOf(maxPlayers));
    sendArenaMessage(ChatColor.DARK_RED + prefix_p + " " + ChatColor.GOLD + message, Integer.parseInt(lines[1]));

    if (playersAmount[arena] == autostartPlayers)
    {
      gameStarted.add(arena);

      getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
      {
        int countdown = 60;

        public void run()
        {
          if (countdown == 0)
          {
            sendArenaMessage(ChatColor.DARK_RED + prefix_p + " " + ChatColor.GOLD + ChatColor.translateAlternateColorCodes('&', messages.getMessage("Arena.CountdownNow")), arena);
            startGame(arena);
            countdown -= 1;
          }
          else if ((countdown > 0) && (countdown < 5))
          {
            String message = ChatColor.translateAlternateColorCodes('&', messages.getMessage("Arena.Countdown"));
            message = message.replaceAll("%seconds", String.valueOf(countdown));

            sendArenaMessage(ChatColor.DARK_RED + prefix_p + " " + ChatColor.GOLD + message, arena);
            countdown -= 1;
          }
          else if (countdown == 10)
          {
            String message = ChatColor.translateAlternateColorCodes('&', messages.getMessage("Arena.Countdown"));
            message = message.replaceAll("%seconds", String.valueOf(countdown));

            sendArenaMessage(ChatColor.DARK_RED + prefix_p + " " + ChatColor.GOLD + message, arena);
            countdown -= 1;
          }
          else if (countdown == 30)
          {
            String message = ChatColor.translateAlternateColorCodes('&', messages.getMessage("Arena.Countdown"));
            message = message.replaceAll("%seconds", String.valueOf(countdown));

            sendArenaMessage(ChatColor.DARK_RED + prefix_p + " " + ChatColor.GOLD + message, arena);
            countdown -= 1;
          }
          else if (countdown == 60)
          {
            String message = ChatColor.translateAlternateColorCodes('&', messages.getMessage("Arena.Countdown"));
            message = message.replaceAll("%seconds", String.valueOf(countdown));

            sendArenaMessage(ChatColor.DARK_RED + prefix_p + " " + ChatColor.GOLD + message, arena);
            countdown -= 1;
          }
          else
          {
            countdown -= 1;
          }
        }
      }
      , 0L, 20L);
    }
  }

  private void teleportStart(int arena)
  {
    Location gameLoc = new Location(Bukkit.getWorld(getConfig().getString("Arenen." + arena + ".spawn.game.world")), getConfig().getInt("Arenen." + arena + ".spawn.game.posX"), getConfig().getInt("Arenen." + arena + ".spawn.game.posY"), getConfig().getInt("Arenen." + arena + ".spawn.game.posZ"), getConfig().getInt("Arenen." + arena + ".spawn.game.pitch"), getConfig().getInt("Arenen." + arena + ".spawn.game.yaw"));

    for (int i = 0; i < maxPlayers; i++)
    {
      if (players[arena][i] != null)
      {
        players[arena][i].teleport(gameLoc);
      }
    }
  }

  public int getArena(Player p)
  {
    int arena = 0;

    for (int i = 0; i < 29; i++)
    {
      for (int m = 0; m < 23; m++)
      {
        if ((players[i][m] != null) && (players[i][m] == p))
        {
          arena = i;
        }
      }
    }

    return arena;
  }

  public int getPlayerNumber(Player p, int arena)
  {
    for (int i = 0; i < 29; i++)
    {
      for (int m = 0; m < 23; m++)
      {
        if ((players[i][m] != null) && (players[i][m] == p))
        {
          return m;
        }
      }
    }
    return 0;
  }

  private void teleportBack(int arena)
  {
    int posX = getConfig().getInt("Arenen." + arena + ".spawn.back.posX");
    int posY = getConfig().getInt("Arenen." + arena + ".spawn.back.posY");
    int posZ = getConfig().getInt("Arenen." + arena + ".spawn.back.posZ");
    float pitch = getConfig().getInt("Arenen." + arena + ".spawn.back.pitch");
    float yaw = getConfig().getInt("Arenen." + arena + ".spawn.back.yaw");
    String world = getConfig().getString("Arenen." + arena + ".spawn.back.world");
    Location backLoc = new Location(Bukkit.getWorld(world), posX, posY, posZ, pitch, yaw);

    for (int i = 0; i < 23; i++)
    {
      if (players[arena][i] != null)
      {
        players[arena][i].teleport(backLoc);
      }
    }
  }

  public String getStatus(int arena)
  {
    if ((playersAmount[arena] < maxPlayers) && (playersAmount[arena] >= 0) && (!gameStarted.contains(Integer.valueOf(arena))))
    {
      return ChatColor.DARK_GREEN + "[JOIN]";
    }
    if (playersAmount[arena] == maxPlayers)
    {
      return ChatColor.RED + "[Full]";
    }
    return ChatColor.DARK_RED + "[ERROR]";
  }

  public void updateSign(int arena, String status, String players)
  {
    if (players.equalsIgnoreCase("")) {
      players = playersAmount[arena] + "/" + maxPlayers;
    }

    Location signLoc = new Location(Bukkit.getWorld(getConfig().getString("Arenen." + arena + ".sign.world")), getConfig().getInt("Arenen." + arena + ".sign.posX"), getConfig().getInt("Arenen." + arena + ".sign.posY"), getConfig().getInt("Arenen." + arena + ".sign.posZ"));
    BlockState zustand = Bukkit.getWorld(getConfig().getString("Arenen." + arena + ".sign.world")).getBlockAt(signLoc).getState();
    Sign schild = (Sign)zustand;
    schild.setLine(2, status);
    schild.setLine(3, players);
    schild.update();
  }

  public void playerRemove(final int arena, Player p)
  {
    playersAmount[arena] -= 1;
    players[arena][getPlayerNumber(p, arena)].getInventory().clear();

    for (Player onPl : getServer().getOnlinePlayers())
    {
      onPl.showPlayer(players[arena][getPlayerNumber(p, arena)]);
    }

    if (gameStarted.contains(arena))
    {
      updateSign(arena, ChatColor.DARK_PURPLE + "[InGame]", "");
    }
    else if (!gameStarted.contains(arena))
    {
      updateSign(arena, ChatColor.DARK_GREEN + "[JOIN]", "");
    }
    if (playersAmount[arena] > 0)
    {
      if (getAliveTeams(arena).size() == 1)
      {
        if (getAliveTeams(arena).contains("3"))
        {
          sendArenaMessage(ChatColor.DARK_RED + prefix_p + " " + ChatColor.GOLD + ChatColor.translateAlternateColorCodes('&', messages.getMessage("Arena.Winnerhumans")), arena);
        }
        else if (getAliveTeams(arena).contains("1"))
        {
          sendArenaMessage(ChatColor.DARK_RED + prefix_p + " " + ChatColor.GOLD + ChatColor.translateAlternateColorCodes('&', messages.getMessage("Arena.WinnerInfected")), arena);
        }

        sendArenaMessage(ChatColor.DARK_RED + prefix_p + " " + ChatColor.GOLD + ChatColor.translateAlternateColorCodes('&', messages.getMessage("Arena.Restart")), arena);
        peacePeriod.add(arena);
        updateSign(arena, ChatColor.RED + "[Restarting]", ChatColor.RED + "\u2588\u2588\u2588\u2588");
        getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable()
        {
          public void run()
          {
            sendArenaMessage(ChatColor.DARK_RED + prefix_p + " " + ChatColor.GOLD + ChatColor.translateAlternateColorCodes('&', messages.getMessage("Arena.RestartNow")), arena);
            peacePeriod.remove(arena);
            stopGame(arena);
          }
        }
        , 400L);
      }
    }
  }

  public List<String> getAliveTeams(int arena)
  {
    List aliveTeams = new ArrayList();
    if (Infected.containsValue(arena))
    {
      aliveTeams.add("1");
    }
    if (scientists.containsValue(arena))
    {
      if (!aliveTeams.contains("3"))
      {
        aliveTeams.add("3");
      }
    }

    if (humans.containsValue(arena))
    {
      if (!aliveTeams.contains("3"))
      {
        aliveTeams.add("3");
      }
    }

    return aliveTeams;
  }

  public void removeItems(int arena)
  {
    World world = Bukkit.getWorld(getConfig().getString("Arenen." + arena + ".spawn.game.world"));

    for (Entity e : world.getEntities())
      if (e.getType() == EntityType.DROPPED_ITEM)
      {
        e.remove();
      }
  }
}

}
