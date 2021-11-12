//
// Created by richa on 12/11/2021.
//

#include <jni.h>

extern "C" JNIEXPORT jfloat JNICALL
Java_com_graphic_main_Vector2_quickInvSqrt(JNIEnv *env, jobject thiz, jfloat l) {
    /*
     * This is the fast inverse square root algorithm.
     * Calculates the value of 1 / sqrt(l) for a given float l in a more efficient way
     * than calculating it straight forward.
     * Source: https://en.wikipedia.org/wiki/Fast_inverse_square_root
     */

    long i;
    float x2, y;
    const float threehalfs = 1.5F;

    x2 = l * 0.5F;
    y  = l;
    i  = * ( long * ) &y;                       // evil floating point bit level hacking
    i  = 0x5f3759df - ( i >> 1 );               // what the fuck?
    y  = * ( float * ) &i;
    y  = y * ( threehalfs - ( x2 * y * y ) );   // 1st iteration
	y  = y * ( threehalfs - ( x2 * y * y ) );   // 2nd iteration, this can be removed

	// Ensure that the value of y is positive
	if (y < 0) {
	    y = -y;
	}

    return y;
}