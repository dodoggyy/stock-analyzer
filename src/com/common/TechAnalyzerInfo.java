/**
 * 
 */
package com.common;

/**
 * @author Chris Lin
 *
 */
public class TechAnalyzerInfo {
    private boolean bIsCCI; // achieve CCI strategy or not
    private boolean bIsKDJ; // achieve KDJ strategy or not
    private boolean bIsBIAS; // achieve BIAS strategy or not
    
    private float mProfitPercentage; // earned interest rate

    public TechAnalyzerInfo() {
        
    }
    /**
     * @return the bIsCCI
     */
    public boolean getIsCCI() {
        return bIsCCI;
    }

    /**
     * @param bIsCCI the bIsCCI to set
     */
    public void setIsCCI(boolean bIsCCI) {
        this.bIsCCI = bIsCCI;
    }

    /**
     * @return the bIsKDJ
     */
    public boolean getIsKDJ() {
        return bIsKDJ;
    }

    /**
     * @param bIsKDJ the bIsKDJ to set
     */
    public void setIsKDJ(boolean bIsKDJ) {
        this.bIsKDJ = bIsKDJ;
    }

    /**
     * @return the bIsBIAS
     */
    public boolean getIsBIAS() {
        return bIsBIAS;
    }

    /**
     * @param bIsBIAS the bIsBIAS to set
     */
    public void setIsBIAS(boolean bIsBIAS) {
        this.bIsBIAS = bIsBIAS;
    }

    /**
     * @return the mProfitPercentage
     */
    public float getProfitPercentage() {
        return mProfitPercentage;
    }

    /**
     * @param mProfitPercentage the mProfitPercentage to set
     */
    public void setProfitPercentage(float mProfitPercentage) {
        this.mProfitPercentage = mProfitPercentage;
    }
    
    public String toString() {
        return new String("bIsCCI: " + bIsCCI + " bIsKDJ: " + bIsKDJ + " bIsBIAS: " + bIsBIAS + " ProfitPercentage: " + mProfitPercentage);
    }
}
