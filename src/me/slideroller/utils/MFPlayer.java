package me.slideroller.utils;

import me.slideroller.scoreboards.MineFectionScoreboard;

import org.bukkit.entity.Player;


public class MFPlayer {
        
        Player player;
        UtilPlayer u;
        int kills;
        int broken;
        private MineFectionScoreboard sb;
        
        public MFPlayer(Player player, UtilPlayer u) {
                this.player = player;
                this.u = u;
                this.kills = 0;
                this.broken = 0;
                this.setScoreboard(new MineFectionScoreboard());
                player.setScoreboard(this.getScoreboard().getScoreboard());
        }
        
        public UtilPlayer getUtilPlayer() {
                return this.u;
        }
        
        public Player getPlayer() {
                return this.player;
        }
        
        public int getKills() {
                return this.kills;
        }
        
        public void setKills(int i) {
                this.kills = i;
        }
        
        public int getBroken() {
                return this.broken;
        }
        
        public void setBroken(int i) {
                this.broken = i;
                getScoreboard().setScore("Broken Blocks", i);
        }
        
        public MineFectionScoreboard getScoreboard() {
                return sb;
        }
        
        public void setScoreboard(MineFectionScoreboard sb) {
                this.sb = sb;
        }
        
}