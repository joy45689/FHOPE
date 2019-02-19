package ncku.asrlab.amber;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class fhope {
    static int [] v ={5,10};
    static int n=2; // n pairs of key and R
    static int sizeofv=v.length;
    static int[][]key;
//    static int [][]R=new int[n][2];
    static List<int [][]> Rs =new ArrayList<>();
    static float[][]c;
    static int xi;

    public static void main (String []arg){
        for(int i=0;i<sizeofv;i++){
            Rs.add(getR(n));
        }
        key=keyGen(n);
        printR(Rs);
        xi=getRandomIntInclusive(0,100,false);
        System.out.println("---xi---\n"+xi);
        c=Encrypt(key,v);
        printC(c);
        Decrypt(c);
        testDec();
    }






    /**
     * KeyGen
     * Generate the key and R
     * @param n pairs of key
     * */
    public static int[][] keyGen(int n){
        List<Integer> primes=primeNumbersBruteForce(0,100);
        int key[][]=new int [n][2];
//        R=getR(n);//全部為正的R
        key=primeRandomChoose(primes,n,true); //key有正有負


        if(Math.random()>0.5){
            for(int i=0;i<key.length;i=i+2){
                key[i][0]=-key[i][0];
                key[i][1]=-key[i][1];
            }
        }else{
            for(int i=1;i<key.length;i=i+2){
                key[i][0]=-key[i][0];
                key[i][1]=-key[i][1];
            }
        }

        /*解法3
        * 先調大小
        * 再做解法1調正負*/
        int R[][];
        for(int j=0;j<Rs.size();j++){
            R=Rs.get(j);
            //2<=i<=n-1
            if(sizeofv>2){
                for(int i=2;i<key.length;i++){
                    if(key[i][0]*key[i-1][0]>0){//若ai跟ai-1相乘是正的,那
                        R[i-1][0]=-R[i-1][0];//ri-1 也一定要為負
                    }
                }
                for(int i=1;i+1<key.length;i++){
                    if(key[i][0]*key[i+1][0]<0){//若ai跟ai+1相乘是負的,那
                        R[i][1]=-R[i][1];//pi 也一定要為負
                    }
                }
            }
            //i=1
            if((key[0][0]*key[1][0])<0){//若a1跟a1相乘是負的,那
                R[0][1]=-R[0][1];       //p1也要是負的
            }
            if((key[0][0]*key[n-1][0])>0){//若a1跟an相乘是正的,那
                R[n-1][0]=-R[n-1][0];     //rn 也一定要為負
            }
            //i=n
            if((key[0][0]*key[n-1][0])<0){//若a1跟an相乘是負的,那
                R[n-1][1]=-R[n-1][1];     //pn 也一定要為負
            }
            if((key[n-1][0]*key[n-2][0])>0){//若an跟an-1相乘是正的,那
                R[n-2][0]=-R[n-2][0];     //rn-1 也一定要為負
            }


            Rs.set(j,R);
        }






        //--------print Key-----------
        System.out.println("---Key---");
        for(int i=0;i<key.length;i++){
            System.out.print(i+1+"( ");
            for(int j=0;j<key[0].length;j++){
                System.out.print(key[i][j]);
                System.out.print(" ");
            }
            System.out.println(")");
        }
//        System.out.println("---------");
        //-------print R----------
//        System.out.println("---R---");
//        for(int i=0;i<R.length;i++){
//            System.out.print(i+1+"( ");
//            for(int j=0;j<R[0].length;j++){
//                System.out.print(R[i][j]);
//                System.out.print(" ");
//            }
//            System.out.println(")");
//        }
        return key;
    }

    /**
     * Encrypt
     * @param key The key to encrypt plaintext
     * @param v The plaintext to be encrypted
     * @return The ciphertext
     * */
    public static float[][] Encrypt(int [][] key,int []v){
        float [][] c = new float[sizeofv][n];
        int s =getS(v);
        int enc;
        int encs;
        float noise;
        System.out.println("---Noise check---");
        for(int i=0;i<sizeofv;i++){
            for(int j=0;j<n;j++){
                enc=enc(key[j][0],key[j][1],v[i]);
                encs=enc(key[j][0],key[j][1],v[i]+s);
                noise=getNoise(key,Rs.get(i),j);
                c[i][j]=enc+noise+xi;
                boolean b=checkNoise(noise,enc,encs);

//                if(!checkNoise(noise,enc,encs)){
//                    System.out.println("Noise error!!!!!");
//                }
                if(b){
//                    System.out.println(j+1+"th");
//                    System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOO");
//                    System.out.println("encS-enc="+(encs-enc));
//                    System.out.println("Noise="+noise);
//                    System.out.println("Key=("+key[j][0]+" "+key[j][1]+")");
//                    System.out.println("R=("+R[j][0]+" "+R[j][1]+")\n");
                }else{
                    System.out.println("\n"+(j+1)+"th");
                    System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
                    System.out.println("encS-enc="+(encs-enc));
                    System.out.println("Noise="+noise);
//                    System.out.println();
//                    System.out.println();
//                    System.out.println();
//                    System.out.println();
//                    System.out.println();

//                    System.out.println("Key=("+key[j][0]+" "+key[j][1]+")");
//                    System.out.println("R=("+R[j][0]+" "+R[j][1]+")\n");
                }

            }
        }
        return c;

    }
    private static void printC(float [][]c){
        System.out.println("----C----");
        for(int i=0;i<c.length;i++){
            System.out.print("{ ");
            for(int j=0;j<c[0].length;j++){
                System.out.print(c[i][j]);
                System.out.print(" ");
            }
            System.out.print("}\n");
        }
    }

    private static void printR(List<int [][]> R){
        System.out.println("---R---");
        for(int i=0;i<R.size();i++){
            int [][]temp=R.get(i);
            for(int j=0;j<temp.length;j++){
                System.out.print(j+1+"( ");
                for(int k=0;k<temp[0].length;k++){
                    System.out.print(temp[j][k]);
                    System.out.print(" ");
                }
                System.out.println(")");
            }
        }
    }






    /**
     * Decryption
     *
     *
     * */
    public static void Decrypt(float c[][]){
        int b[]=new int[n];
        float temp[][]=new float[sizeofv][n];
        float plaintext[]=new float[sizeofv];
        for(int i=0;i<key.length;i++){
            b[i]=key[i][1];
        }
        for(int i=0;i<c.length;i++){ //i=0~sizeofv-1
            for(int j=0;j<c[0].length;j++){ // j=0~n-1
                temp[i][j]=c[i][j]/key[j][0];
            }
        }
        System.out.println("---解密後---");
        for(int i=0;i<c.length;i++){
            plaintext[i]=sum(temp[i])/sum(b);
            System.out.println(plaintext[i]);
        }
    }

    public static void testDec(){
        int a[]=new int[n];
        int b[]=new int[n];
        for(int i=0;i<key.length;i++){
            a[i]=key[i][0];
            b[i]=key[i][1];
        }

        float A,B,C;
        System.out.println("---testDec---");
        // part A
        float tempA[]=new float[n];
        for(int i=0;i<n;i++){
            tempA[i]=(a[i]*b[i]*v[0])/(float)(a[i]*sum(b));
        }
        A=sum(tempA);
        System.out.println("A:"+A);

        // part B
        float tempB[]=new float[n];
        for(int i=0;i<n;i++){
            tempB[i]=getNoise(key,Rs.get(0),i)/(float)(a[i]*sum(b));
        }
        B=sum(tempB);
        System.out.println("B:"+B);

        // part C
        float tempC[]=new float[n];
        for(int i=0;i<n;i++){
            tempC[i]=xi/(float)(a[i]*sum(b));
        }
        C=sum(tempC);
        System.out.println("C:"+C);




//        System.out.println("Output:"+A+B+C);

    }








    /**
     * Enc(a,b,v)=a*b*v
     * @param a The i th a of K(n)
     * @param b The i th b of K(n)
     * @param v The i th plaintext
     * */
    private static int enc(int a,int b, int v){
        return a*b*v;
    }

    private static boolean checkNoise(double noise,int enc,int encS){
        if(noise>0&&(encS-enc)>noise){
            return true;
        }else{
            return false;
        }
    }
    /**
     * Noise(a,r,p)
     * @param key (a,b)
     * @param R (r,p)
     * @param i The i th i=0~n-1
     * @return the i th noise
     * **/
    public static float getNoise(int [][] key,int [][] R,int i){
        float noise=0;
        if(i==0){ //i=1
            noise=((key[0][0]*R[0][1])/(float)key[1][0])-((key[0][0]*R[n-1][0])/(float)key[n-1][0])+R[0][0]-R[n-1][1];

        }else if(i==n-1){ //i=n
            noise=((key[n-1][0]*R[n-1][1])/(float)key[0][0])-((key[n-1][0]*R[n-2][0])/(float)key[n-2][0])+R[n-1][0]-R[n-2][1];

        }else{ //2 <= i <= n-1
            noise=((key[i][0]*R[i][1])/(float)key[i+1][0])-((key[i][0]*R[i-1][0])/(float)key[i-1][0])+R[i][0]-R[i-1][1];
        }

//        if(i==0){ //i=1
//            noise=((key[0][0]*R[0][1])/key[1][0])-((key[0][0]*R[n-1][0])/key[n-1][0])+R[0][0]-R[n-1][1];
//
//        }else if(i==n-1){ //i=n
//            noise=((key[n-1][0]*R[n-1][1])/key[0][0])-((key[n-1][0]*R[n-2][0])/key[n-2][0])+R[n-1][0]-R[n-2][1];
//
//        }else{ //2 <= i <= n-1
//            noise=((key[i][0]*R[i][1])/key[i+1][0])-((key[i][0]*R[i-1][0])/key[i-1][0])+R[i][0]-R[i-1][1];
//        }

        System.out.println("Noise:"+noise);
        return noise;

    }


    /**
     * 從 primes中選出2n個質數
     * @param primes 質數List
     * @param n n個pairs
     * */
    private static int [][] primeRandomChoose(List<Integer> primes,int n,boolean isAllPositive){
        int key[][]=new int[n][2];
        int ram;

        for(int i=0;i<key.length;i++){
            for(int j=0;j<key[0].length;j++){
                ram=getRandomIntInclusive(0,primes.size()-1,true);
                key[i][j]=primes.get(ram);
            }
        }
//        隨機正負號
        if(!isAllPositive){
            for(int i=0;i<key.length;i++){
                if(Math.random()>0.5){
                    key[i][0]=-key[i][0];
                    key[i][1]=-key[i][1];
                }
            }
        }
        return key;
    }

    private static int [][] getR(int n){
        //TODO 讓r很大只有正;p小且有正有負
        int [][] R=new int[n][2];
//        for(int i=0;i<R.length;i++){
//            for(int j=0;j<R[0].length;j++){
//                R[i][j]=getRandomIntInclusive(0,500,true);//先讓全部為正
//            }
//        }
        for(int i=0;i<R.length;i++){
            R[i][0]=getRandomIntInclusive(0,100,true);//r
            R[i][1]=getRandomIntInclusive(0,100,true);//p
        }





//        System.out.println("---R---");
//        for(int i=0;i<R.length;i++){
//            System.out.print("( ");
//            for(int j=0;j<R[0].length;j++){
//                System.out.print(R[i][j]);
//                System.out.print(" ");
//            }
//            System.out.println(")");
//        }
        return R;
    }
    /**
     * 算一個陣列中的sensitivity
     *
     * */
    private static int getS(int []input){
        int s= Integer.MAX_VALUE;
        //暴力法
        //TODO 改成不用暴力法
        for(int i=0;i<input.length;i++){
            for(int j=i+1;j<input.length;j++){
                if (Math.abs(input[i]-input[j])<s){
                    s=Math.abs(input[i]-input[j]);
                }
            }
        }
        System.out.println("---S---");
        System.out.println(s);


        return s;
    }

    /**
     * 取得min與max間隨機數
     * @param min 最小值
     * @param max 最大值
     * @param isAllPositive 隨機產生的數字是否全部都要正的
     * https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Global_Objects/Math/random
     */
    private static int getRandomIntInclusive(int min, int max, boolean isAllPositive) {
        double ram=Math.random();
        min = (int)Math.ceil(min);
        max = (int)Math.floor(max);
        int temp;
        if(max==Integer.MAX_VALUE){
            temp=(int)Math.floor(ram * max);
        }else{
            temp=(int)Math.floor(ram * (max-min)+1);
        }

        if(!isAllPositive){ //要的值有正有負
            if(Math.random()>0.5){
                temp=-temp;
            }
        }
        int result=(temp + min);
        return result; //The maximum is inclusive and the minimum is inclusive
    }

//    function getRandomIntInclusive(min, max) {
//        min = Math.ceil(min);
//        max = Math.floor(max);
//        return Math.floor(Math.random() * (max - min + 1)) + min; //The maximum is inclusive and the minimum is inclusive
//    }
//


    /**
     * prime generator
     * https://www.baeldung.com/java-generate-prime-numbers
     * @param s start
     * @param e end
     * @return 從s到e之間的質數
     * */
    //TODO 添加正負數
    private static List<Integer> primeNumbersBruteForce(int s,int e) {
        List<Integer> primeNumbers = new LinkedList<>();
//        if (n >= 2) {
//            primeNumbers.add(2);
//        }
        if(s==0){
            s=3;
            primeNumbers.add(2);
        }else if(s%2==0){
            s=s+11;
        }
        for (int i = s; i <= e; i += 2) {
            if (isPrimeBruteForce(i)) {
                primeNumbers.add(i);
            }
        }
        return primeNumbers;
    }
    /**
     * 確認是否是質數
     * @param number 待確認的數字
     * @return true or false
     * */
    private static boolean isPrimeBruteForce(int number) {
        for (int i = 2; i*i < number; i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 陣列元素累加
     * @param input 欲壘加的陣列
     * */
    private static int sum(int []input){
        int length=input.length;
        int sum=0;
        for(int i=0;i<length;i++){
            sum +=input[i];
        }
        return sum;
    }

    private static float sum(float []input){
        int length=input.length;
        float sum=0;
        for(int i=0;i<length;i++){
            sum +=input[i];
        }
        return sum;
    }




}
