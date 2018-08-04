package DataTypes.Util;

import DataTypes.GeneratedMachineDataTypes.Battlefield;

public class BattlefieldWrapper {
    private Battlefield m_battlefield;

    public BattlefieldWrapper() {
    }

    public BattlefieldWrapper(Battlefield m_battlefield) {
        this.m_battlefield = m_battlefield;
    }

    /****************************************
     *       XML BATTLEFIELD FUNCTIONS
     *****************************************/
    /**
     * Gets the value of the rounds property.
     *
     */
    public int getRounds() {
        return m_battlefield.getRounds();
    }

    /**
     * Sets the value of the rounds property.
     *
     */
    public void setRounds(int value) {
        m_battlefield.setRounds(value);
    }

    /**
     * Gets the value of the level property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getLevel() {
        return m_battlefield.getLevel();
    }

    /**
     * Sets the value of the level property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setLevel(String value) {
        m_battlefield.setLevel(value);
    }

    /**
     * Gets the value of the battleName property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getBattleName() {
        return m_battlefield.getBattleName();
    }

    /**
     * Sets the value of the battleName property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setBattleName(String value) {
        m_battlefield.setBattleName(value);
    }

    /**
     * Gets the value of the allies property.
     *
     */
    public int getAllies() {
        return m_battlefield.getAllies();
    }

    /**
     * Sets the value of the allies property.
     *
     */
    public void setAllies(int value) {
        m_battlefield.setAllies(value);
    }


}
