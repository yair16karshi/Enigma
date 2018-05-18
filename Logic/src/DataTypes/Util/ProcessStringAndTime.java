package DataTypes.Util;

import java.util.Date;

public class ProcessStringAndTime {
    private String m_unprocessedString;
    private String m_processedString;
    private long time;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getUnprocessedString() {
        return m_unprocessedString;
    }

    public void setUnprocessedString(String m_unprocessedString) {
        this.m_unprocessedString = m_unprocessedString;
    }

    public String getProcessedString() {
        return m_processedString;
    }

    public void setProcessedString(String m_processedString) {
        this.m_processedString = m_processedString;
    }

    public String toString() {
        return "<" + this.m_unprocessedString + "> --> <" + this.m_processedString+ "> (" + this.time +")";
    }
}