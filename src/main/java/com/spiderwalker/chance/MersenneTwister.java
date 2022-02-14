package com.spiderwalker.chance;

public class MersenneTwister {
    // Mersenne Twister from https://gist.github.com/banksean/300494
    /*
     * A C-program for MT19937, with initialization improved 2002/1/26.
     * Coded by Takuji Nishimura and Makoto Matsumoto.
     * 
     * Before using, initialize the state by using init_genrand(seed)
     * or init_by_array(init_key, key_length).
     * 
     * Copyright (C) 1997 - 2002, Makoto Matsumoto and Takuji Nishimura,
     * All rights reserved.
     * 
     * Redistribution and use in source and binary forms, with or without
     * modification, are permitted provided that the following conditions
     * are met:
     * 
     * 1. Redistributions of source code must retain the above copyright
     * notice, this list of conditions and the following disclaimer.
     * 
     * 2. Redistributions in binary form must reproduce the above copyright
     * notice, this list of conditions and the following disclaimer in the
     * documentation and/or other materials provided with the distribution.
     * 
     * 3. The names of its contributors may not be used to endorse or promote
     * products derived from this software without specific prior written
     * permission.
     * 
     * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
     * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
     * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
     * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
     * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
     * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
     * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
     * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
     * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
     * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
     * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
     * 
     * 
     * Any feedback is very welcome.
     * http://www.math.sci.hiroshima-u.ac.jp/~m-mat/MT/emt.html
     * email: m-mat @ math.sci.hiroshima-u.ac.jp (remove space)
     */

    public MersenneTwister() {
        // kept random number same size as time used previously to ensure no unexpected
        // results downstream
        int seed = (int)Math.floor(Math.random() * Math.pow(10, 13));
        initPeriodParameter(seed);
    }

    public MersenneTwister(int seed) {
        initPeriodParameter(seed);
    };

    /* initializes mt[N] with a seed */
    private int N;
    private int M;
    private int MATRIX_A;
    private int UPPER_MASK;
    private int LOWER_MASK;
    private int mt[];
    private int mti;

    
    private void initPeriodParameter(int seed) {
        /* Period parameters */
        N = 624;
        M = 397;
        MATRIX_A = 0x9908b0df; /* constant vector a */
        UPPER_MASK = 0x80000000; /* most significant w-r bits */
        LOWER_MASK = 0x7fffffff; /* least significant r bits */

        mt =new int[N]; /* the array for the state vector */
        mti = N + 1; /* mti==N + 1 means mt[N] is not initialized */

        init_genrand(seed);
    }

    public void init_genrand (int s) {
        mt[0] = s >>> 0;
        for (mti = 1; mti < N; mti++) {
            s = mt[mti - 1] ^ (mt[mti - 1] >>> 30);
            mt[mti] = (((((s & 0xffff0000) >>> 16) * 1812433253) << 16) + (s & 0x0000ffff) * 1812433253) + mti;
            /* See Knuth TAOCP Vol2. 3rd Ed. P.106 for multiplier. */
            /* In the previous versions, MSBs of the seed affect   */
            /* only MSBs of the array mt[].                        */
            /* 2002/01/09 modified by Makoto Matsumoto             */
            mt[mti] >>>= 0;
            /* for >32 bit machines */
        }
    };

    /* initialize by an array with array-length */
    /* init_key is the array for initializing keys */
    /* key_length is its length */
    /* slight change for C++, 2004/2/26 */
    public void init_by_array (int init_key[], int key_length) {
        int i = 1, j = 0, k, s;
        init_genrand(19650218);
        // k = (N > key_length ? N : key_length);
        // for (; ; k--) {
        //     s = mt[i - 1] ^ (mt[i - 1] >>> 30);
        //     mt[i] = (mt[i] ^ (((((s & 0xffff0000) >>> 16) * 1664525) << 16) + ((s & 0x0000ffff) * 1664525))) + init_key[j] + j; /* non linear */
        //     mt[i] >>>= 0; /* for WORDSIZE > 32 machines */
        //     i++;
        //     j++;
        //     if (i >= N) { mt[0] = mt[N - 1]; i = 1; }
        //     if (j >= key_length) { j = 0; }
        // }
        // for (k = N - 1; ; k--) {
        //     s = mt[i - 1] ^ (mt[i - 1] >>> 30);
        //     mt[i] = (mt[i] ^ (((((s & 0xffff0000) >>> 16) * 1566083941) << 16) + (s & 0x0000ffff) * 1566083941)) - i; /* non linear */
        //     mt[i] >>>= 0; /* for WORDSIZE > 32 machines */
        //     i++;
        //     if (i >= N) { mt[0] = mt[N - 1]; i = 1; }
        // }

        // mt[0] = 0x80000000; /* MSB is 1; assuring non-zero initial array */
    }

    /* generates a random number on [0,0xffffffff]-interval */
    public int genrand_int32 () {
        int y;
        int[] mag01 =  {0x0, MATRIX_A};
        /* mag01[x] = x * MATRIX_A  for x=0,1 */

        if (mti >= N) { /* generate N words at one time */
            int kk;

            if (mti == N + 1) {   /* if init_genrand() has not been called, */
                init_genrand(5489); /* a default initial seed is used */
            }
            for (kk = 0; kk < N - M; kk++) {
                y = (mt[kk]&UPPER_MASK)|(mt[kk + 1]&LOWER_MASK);
                mt[kk] = mt[kk + M] ^ (y >>> 1) ^ mag01[y & 0x1];
            }
            for (;kk < N - 1; kk++) {
                y = (mt[kk]&UPPER_MASK)|(mt[kk + 1]&LOWER_MASK);
                mt[kk] = mt[kk + (M - N)] ^ (y >>> 1) ^ mag01[y & 0x1];
            }
            y = (mt[N - 1]&UPPER_MASK)|(mt[0]&LOWER_MASK);
            mt[N - 1] = mt[M - 1] ^ (y >>> 1) ^ mag01[y & 0x1];

            mti = 0;
        }

        y = mt[mti++];

        /* Tempering */
        y ^= (y >>> 11);
        y ^= (y << 7) & 0x9d2c5680;
        y ^= (y << 15) & 0xefc60000;
        y ^= (y >>> 18);

        return y >>> 0;
    };

    /* generates a random number on [0,0x7fffffff]-interval */
    public int genrand_int31 () {
        return (genrand_int32() >>> 1);
    };

    /* generates a random number on [0,1]-real-interval */
    public int genrand_real1 () {
        return (int) (genrand_int32() * (1.0 / 4294967295.0));
        /* divided by 2^32-1 */
    };

    /* generates a random number on [0,1)-real-interval */
    public int random () {
        return (int) (genrand_int32() * (1.0 / 4294967296.0));
        /* divided by 2^32 */
    };

    /* generates a random number on (0,1)-real-interval */
    public int genrand_real3 () {
        return (int) ((genrand_int32() + 0.5) * (1.0 / 4294967296.0));
        /* divided by 2^32 */
    };

    /* generates a random number on [0,1) with 53-bit resolution */
    public int genrand_res53() {
        int a = genrand_int32() >>> 5, b = genrand_int32() >>> 6;
        return (int) ((a * 67108864.0 + b) * (1.0 / 9007199254740992.0));
    };
}
