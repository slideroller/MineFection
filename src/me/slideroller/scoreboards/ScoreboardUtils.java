package me.slideroller.scoreboards;

import me.slideroller.arena.Game;
import me.slideroller.utils.MFPlayer;


public class ScoreboardUtils {
        
        private static ScoreboardUtils ins = new ScoreboardUtils();
        
        public static ScoreboardUtils get() {
                return ins;
        }
        
        public void setDisplayAll(Game game, String name) {
                for (MFPlayer sp : game.getPlayers().values()) {
                        sp.getScoreboard().setDisplayName(name);
                }
        }
        
        public void setScoreAll(Game game, String iden, int score) {
                for (MFPlayer sp : game.getPlayers().values()) {
                        sp.getScoreboard().setScore(iden, score);
                }
        }
        
        public void hideScoreAll(Game game, String iden) {
                for (MFPlayer sp : game.getPlayers().values()) {
                        sp.getScoreboard().hideScore(iden);
                }
        }
        
}

//DO NOT FIX THE ERROR. IT WILL BE FIX IT'S SELF ONCE THE GAME IS ADDED JUST THE .VALUES IN THE GAME AND IT'S FIXED//
