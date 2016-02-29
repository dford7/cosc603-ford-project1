
/**
 *
 * @author Daniel 
 */
public class Project1 {
    public static void main(String[] args) {
        

    }
    
    public static ReturnedData danger(double dry, double wet,
            boolean isSnow, double precip, 
            double wind, double buo, int iHerb,
            double df, double ffm, double adfm,
            int grass, int timber, double fload
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
        
        if (isSnow) {
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
        
        return new ReturnedData(df, ffm, adfm, grass, timber, fload, buo);
    }
    
    public static class ReturnedData{
        private double df;
        private double ffm;
        private double adfm;        
        private int grass;
        private int timber;
        private double fload;  
        private double buo;

        public ReturnedData(double df, double ffm, double adfm, int grass, int timber, double fload, double buo) {
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
