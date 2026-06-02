package com.earth2me.essentials.signs;

public enum Signs
{
    BALANCE((EssentialsSign)new SignBalance()), 
    BUY((EssentialsSign)new SignBuy()), 
    DISPOSAL((EssentialsSign)new SignDisposal()), 
    FREE((EssentialsSign)new SignFree()), 
    HEAL((EssentialsSign)new SignHeal()), 
    MAIL((EssentialsSign)new SignMail()), 
    PROTECTION((EssentialsSign)new SignProtection()), 
    SELL((EssentialsSign)new SignSell()), 
    SPAWNMOB((EssentialsSign)new SignSpawnmob()), 
    TIME((EssentialsSign)new SignTime()), 
    TRADE((EssentialsSign)new SignTrade()), 
    WARP((EssentialsSign)new SignWarp()), 
    WEATHER((EssentialsSign)new SignWeather()),

    // custom signs
    LANDMARK(new SignLandmark());
    
    private final EssentialsSign sign;
    
    private Signs(final EssentialsSign sign) {
        this.sign = sign;
    }
    
    public EssentialsSign getSign() {
        return this.sign;
    }
}
