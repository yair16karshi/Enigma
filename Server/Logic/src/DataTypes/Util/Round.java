package DataTypes.Util;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by yairk on Aug, 2018
 */
public class Round {
    private Set<String> winners;
    private Boolean ReadyToStart;
    private boolean finished;
    private String msgAfterDecipher;
    private String msgBeforeDecipher;

    public Round() {
        ReadyToStart = new Boolean(false);
        this.winners = new HashSet<>();
    }

    public void addWinner(String winnerName) {
        winners.add(winnerName);
    }

    public void setReadyToStart(boolean readyToStart) {
        synchronized (ReadyToStart){
            ReadyToStart = readyToStart;
        }
    }
}
