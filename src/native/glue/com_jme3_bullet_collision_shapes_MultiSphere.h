/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_jme3_bullet_collision_shapes_MultiSphere */

#ifndef _Included_com_jme3_bullet_collision_shapes_MultiSphere
#define _Included_com_jme3_bullet_collision_shapes_MultiSphere
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_jme3_bullet_collision_shapes_MultiSphere
 * Method:    countSpheres
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_jme3_bullet_collision_shapes_MultiSphere_countSpheres
  (JNIEnv *, jobject, jlong);

/*
 * Class:     com_jme3_bullet_collision_shapes_MultiSphere
 * Method:    createShape
 * Signature: ([Lcom/jme3/math/Vector3f;[FI)J
 */
JNIEXPORT jlong JNICALL Java_com_jme3_bullet_collision_shapes_MultiSphere_createShape
  (JNIEnv *, jobject, jobjectArray, jfloatArray, jint);

/*
 * Class:     com_jme3_bullet_collision_shapes_MultiSphere
 * Method:    createShapeB
 * Signature: (Ljava/nio/ByteBuffer;I)J
 */
JNIEXPORT jlong JNICALL Java_com_jme3_bullet_collision_shapes_MultiSphere_createShapeB
  (JNIEnv *, jobject, jobject, jint);

/*
 * Class:     com_jme3_bullet_collision_shapes_MultiSphere
 * Method:    getSpherePosition
 * Signature: (JILcom/jme3/math/Vector3f;)V
 */
JNIEXPORT void JNICALL Java_com_jme3_bullet_collision_shapes_MultiSphere_getSpherePosition
  (JNIEnv *, jobject, jlong, jint, jobject);

/*
 * Class:     com_jme3_bullet_collision_shapes_MultiSphere
 * Method:    getSphereRadius
 * Signature: (JI)F
 */
JNIEXPORT jfloat JNICALL Java_com_jme3_bullet_collision_shapes_MultiSphere_getSphereRadius
  (JNIEnv *, jobject, jlong, jint);

#ifdef __cplusplus
}
#endif
#endif
