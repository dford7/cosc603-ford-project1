
// TODO: Auto-generated Javadoc
/**
 * The Class Project1.
 *
 * @author Daniel
 */
public class Project1 {
    
    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        
        
    }
    
    /**
     * danger function 
     * ROUTINEFOR COMPUTING NATIONAL FIRE DANGER RATINGS AND FIRE LOAD INDEX.
     *
     * @param dry   DRY BULB TEMPERATE
     * @param wet   WET BULB TEMPERATE
     * @param isSnow SOME POSITIVENON ZERO NUMBER IF THERE IS SNOW ON THE GROUN
     * @param precip RAINY
     * @param wind  THE CURRENTWIND SPEED IN MILES PER HOUR
     * @param buo   THE LASTVALUE OF THE BUILD UP INDE
     * @param iHerb THE CURRENTHERB STATE OF THE DISTRICTI=CURED,2=TRANSITION,3=GREE
     * @param df    DRYING FACTOR
     * @param ffm   FINE FUEL MOISTUR
     * @param adfm  ADJUSTED(10 DAY LAG) FUEL MOISTUR
     * @param grass GRASSSPREAD INDE
     * @param timber TIMBER SPREAD INDE
     * @param fload FIRE LOAD RATING (MAN-HOURBASE)
     * @return ReturnedData
     */
    public static ReturnedData danger(double dry, double wet,
            int isSnow, double precip, 
            double wind, double buo, int iHerb,
            double df, double ffm, double adfm,
            double grass, double timber, double fload
            ){
        double [] A = new double[4];
        double [] B = new double[4];
        double [] C = new double[3];
        double [] D = new double[6];
        
        ffm = 99.0;
        adfm = 99.0;
        df   = 0.0;
        fload = 0.0;       
       
        A[0] = -0.185900 ;
        A[1] = -0.85900 ;
        A[2] = -0.059660 ;
        A[3] = -0.077373 ;
        B[0] = 30.0 ; 
        B[1] = 19.2 ;
        B[2] = 13.8 ;
        B[3] = 22.5 ;
        C[0] = 4.5 ;
        C[1] = 12.5 ;
        C[2] = 27.5 ;
        D[0] = 16.0 ;
        D[1] = 10.0 ; 
        D[2]=  7.0 ;
        D[3] = 5.0 ;
        D[4] = 4.0 ; 
        D[5] = 3.0 ;
        
        if (isSnow > 0) {
            // THERE IS SNOW ON THE GROUND'ANDTHE TIMBER AND GRASS SPREAD INDEXE
            // MUST BE SET TO ZERO. WITH A ZERO TIMBERSPREAD THE FIRE LOAD IS
            // ALSO ZERO. BUILD UP WILL BE ADJUSTED FOR PRECIPITATIO
            grass = 0;
            timber = 0;
            if (precip >= 0.1 ) {
                buo = -50.0 * Math.log( 1.0 - Math.exp(buo/50.0) ) * Math.exp(1.175 * (precip - 1 ));
                if (buo < 0) {                    
                    buo = 0;
                }
            }   
            return new ReturnedData(df, ffm, adfm, grass, timber, fload, buo);
 
        } 
        //  HERE IS NO SNOW ON THE GRO_D AND WE WILL COMPUTETHE SPREAD INDEXE
        // ND FIRE LOAD

        double dif = dry - wet;
        for (int i = 0; i < C.length; i++) {
            if (dif > C[i]) {
                ffm = B[i] * Math.exp(A[i] * dif);
            }                
        }

        /**
         * WE WILL NOW FIND THE DRYING FACTORFOR THE DAY
         */           
        for (int i = 0; i < D.length; i++) {
            if (ffm > D[i]) {
                df = i - 1;
                /**
                 * TEST TO SEE IF THE FINE FUEL MOISTUREIS ONE OR LESS
                 * TEST TO SEE IF THE FINE FUEL MOISTUREIS ONE OR LESS
                 * IF FINE FUEL MOISTURE IS ONE OR LESS WE SET IT TO ON
                 */
                if (ffm < 1.0) {
                    ffm = 1.0;
                }
                /**
                 * ADD 5 PERCENT FINE FUEL MOISTURE FOR EACH 
                 * HERB STAGE GREATER THAN ONE
                 */
                ffm += (iHerb - 1) * 5;
            }                
        }

        /**
         * WE MUST ADJUST THE BUI FOR PRECIPITATION BEFORE 
         * ADDING THE DRYING FACTO
         */
        if (precip >= 1.0) {
            /**
             * PRECIPITATION EXCEEDED 0.10 INCHES WE MUST REDUCE THR
             * BUILD UP INDEX (BUO) BY AN AMOUNT EQUAL TO THE RAIN FALL
             */
            buo = -50.0 * Math.log( 1.0 - Math.exp(buo/50.0) ) * Math.exp(1.175 * (precip - 1 ));
            if (buo > 0) {
                buo = 0.0;
                /**
                 * AFTER CORRECTION FOR RAIN, 
                 * IF ANY, WE ARE READY TO ADD TODAY'S
                 * DRYING FACTOR TO OBTAIN THE CURRENT BUILD UP INDE
                 */
                buo += df ;
            }
        }

        /**
         * WE WILL ADJUST THE GRASS SPREAD INDEX FOR HEAVY FUEL LAGS
         * THE RESULT WILL BE THE TIMBERSPREAD INDE
         * THE ADJUSTED FUEL MOISTURE, ADFM, ADJUSTED FOR HEAVY FUELS, WILL
         * NOW BE COMPUTE
         */
        adfm = 0.9 * ffm + 0.5 + 9.5 * Math.exp(buo/ (-50.0));
        /**
         * TEST TO SEE IF THE FUEL MOISTURESARE GREATER THAN 30 PERCENT
         * IF THEY ARE, SET THEIR INDEX VALUES TO I
         */
        if (adfm >= 30.0) {
            if (ffm >= 30.0) {
                /**
                 * FINE FUEL MOISTURE IS GREATERTHAN 30 PERCENT, 
                 * THUS WE SET THE GRASS AND TIMBER SPREAD INDEXES TO ONE                     */
                grass = 1;
                timber = 1;
                return new ReturnedData(df, ffm, adfm, grass, timber, fload, buo);
            }   
            timber = 1;            
        }
        /**
        * TEST TO SEE IF THE WIND SPEED IS GREATERTHAN 14 M
        */
        if (wind < 14.0) {
            timber = 0.01312 * (wind + 6.0) * Math.pow(33.0 - adfm, 1.65) - 3.0;
            grass = 0.01312 * (wind + 6.0) * Math.pow(33.0 - ffm, 1.65) - 3.0;
            if (timber >= 1) {
                timber = 1.0;
            }
            if (grass >= 1.0) {
                grass = 1.0;
            }
        } else {
            /**
             * WIND SPEED IS GREATER THAN 14 MPH. WE USE A DIFFERENTFORMUL
             */
            timber = 0.00918 * (wind + 14.0) * Math.pow(33.0 - adfm, 1.65) - 3.0;
            grass = 0.00918 * (wind + 14.0) * Math.pow(33.0 - ffm, 1.65) - 3.0;
            if (grass >= 99.0 ) {
                grass = 99.0;
            }
            if (timber >= 99.0) {
                timber = 99.0;
            }
        }
       /**
        * WE HAVE NOW COMPUTEDTHE GRASS AND TIMBER SPREAD INDEXE
        * OF THE NATIONAL FIRE DANGER RATING SYSTEM. WE HAVE TH
        * BUILD UP INDEX AND NOW WE WILL COMPUTETHE FIRE LOAD RATING
        */
        if (timber <= 0 || buo <= 0) {
            
                /**
                 * IT IS NECESSARYTHAT NEITHER TIMBER SPREAD NOR BUILD UP BE ZER
                 * IF EITHER TIMBER SPREAD OR BUILD UP IS ZERO, FIRE LOAD IS ZER
                 */
                return new ReturnedData(df, ffm, adfm, grass, timber, fload, buo);                        
        }        
        /**
         * BOTH TIMBER SPREAD AND BUILD UP ARE GREATER THAN ZER
         */
        fload = 1.75 * Math.log10(timber) + 0.32 * Math.log10(buo) - 1.640;
        /**
         * ENSURE THAT FLOAD IS GREATER THAN ZERO, OTHERWISESET IT TO ZERO
         */
        if (fload < 0) {
            fload = 0;
            return new ReturnedData(df, ffm, adfm, grass, timber, fload, buo);
        }
        
        fload = Math.pow(10, fload);            
               
        return new ReturnedData(df, ffm, adfm, grass, timber, fload, buo);
    }
    
    /**
     * returned data from the function danger.
     */
    public static class ReturnedData{
        
        /** The df. */
        private double df;
        
        /** The ffm. */
        private double ffm;
        
        /** The adfm. */
        private double adfm;        
        
        /** The grass. */
        private double grass;
        
        /** The timber. */
        private double timber;
        
        /** The fload. */
        private double fload;  
        
        /** The buo. */
        private double buo;

        /**
         * Constructor .
         *
         * @param df    DRYING FACTOR
         * @param ffm   FINE FUEL MOISTUR
         * @param adfm  ADJUSTED(10 DAY LAG) FUEL MOISTUR
         * @param grass GRASSSPREAD INDE
         * @param timber TIMBER SPREAD INDE
         * @param fload FIRE LOAD RATING (MAN-HOURBASE)
         * @param buo THE LASTVALUE OF THE BUILD UP INDE
         */
        public ReturnedData(double df, double ffm, double adfm, double grass, double timber, double fload, double buo) {
            this.df = df;
            this.ffm = ffm;
            this.adfm = adfm;
            this.grass = grass;
            this.timber = timber;
            this.fload = fload;
            this.buo = buo;
        }
       
    }
    
}
