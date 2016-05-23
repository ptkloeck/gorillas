package de.ptkapps.gorillas;

public interface ActionResolver {
    
    public void showOrLoadInterstital();
    
    public boolean getSignedInGPGS();
    
    public void loginGPGS();
 
    public void unlockAchievementGPGS(String achievementId);
    
    public void getAchievementsGPGS();
}
