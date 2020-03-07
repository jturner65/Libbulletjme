/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_jme3_bullet_MultiBodyLink */

#ifndef _Included_com_jme3_bullet_MultiBodyLink
#define _Included_com_jme3_bullet_MultiBodyLink
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_jme3_bullet_MultiBodyLink
 * Method:    addConstraintForce
 * Signature: (JLcom/jme3/math/Vector3f;)V
 */
JNIEXPORT void JNICALL Java_com_jme3_bullet_MultiBodyLink_addConstraintForce
  (JNIEnv *, jobject, jlong, jobject);

/*
 * Class:     com_jme3_bullet_MultiBodyLink
 * Method:    addContraintTorque
 * Signature: (JLcom/jme3/math/Vector3f;)V
 */
JNIEXPORT void JNICALL Java_com_jme3_bullet_MultiBodyLink_addContraintTorque
  (JNIEnv *, jobject, jlong, jobject);

/*
 * Class:     com_jme3_bullet_MultiBodyLink
 * Method:    addForce
 * Signature: (JLcom/jme3/math/Vector3f;)V
 */
JNIEXPORT void JNICALL Java_com_jme3_bullet_MultiBodyLink_addForce
  (JNIEnv *, jobject, jlong, jobject);

/*
 * Class:     com_jme3_bullet_MultiBodyLink
 * Method:    addJointTorque
 * Signature: (JIF)V
 */
JNIEXPORT void JNICALL Java_com_jme3_bullet_MultiBodyLink_addJointTorque
  (JNIEnv *, jobject, jlong, jint, jfloat);

/*
 * Class:     com_jme3_bullet_MultiBodyLink
 * Method:    addTorque
 * Signature: (JLcom/jme3/math/Vector3f;)V
 */
JNIEXPORT void JNICALL Java_com_jme3_bullet_MultiBodyLink_addTorque
  (JNIEnv *, jobject, jlong, jobject);

/*
 * Class:     com_jme3_bullet_MultiBodyLink
 * Method:    getAppliedForce
 * Signature: (JLcom/jme3/math/Vector3f;)V
 */
JNIEXPORT void JNICALL Java_com_jme3_bullet_MultiBodyLink_getAppliedForce
  (JNIEnv *, jobject, jlong, jobject);

/*
 * Class:     com_jme3_bullet_MultiBodyLink
 * Method:    getAppliedTorque
 * Signature: (JLcom/jme3/math/Vector3f;)V
 */
JNIEXPORT void JNICALL Java_com_jme3_bullet_MultiBodyLink_getAppliedTorque
  (JNIEnv *, jobject, jlong, jobject);

/*
 * Class:     com_jme3_bullet_MultiBodyLink
 * Method:    getAxisBottom
 * Signature: (JILcom/jme3/math/Vector3f;)V
 */
JNIEXPORT void JNICALL Java_com_jme3_bullet_MultiBodyLink_getAxisBottom
  (JNIEnv *, jobject, jlong, jint, jobject);

/*
 * Class:     com_jme3_bullet_MultiBodyLink
 * Method:    getAxisTop
 * Signature: (JILcom/jme3/math/Vector3f;)V
 */
JNIEXPORT void JNICALL Java_com_jme3_bullet_MultiBodyLink_getAxisTop
  (JNIEnv *, jobject, jlong, jint, jobject);

/*
 * Class:     com_jme3_bullet_MultiBodyLink
 * Method:    getCollider
 * Signature: (JI)J
 */
JNIEXPORT jlong JNICALL Java_com_jme3_bullet_MultiBodyLink_getCollider
  (JNIEnv *, jobject, jlong, jint);

/*
 * Class:     com_jme3_bullet_MultiBodyLink
 * Method:    getConstraintForce
 * Signature: (JLcom/jme3/math/Vector3f;)V
 */
JNIEXPORT void JNICALL Java_com_jme3_bullet_MultiBodyLink_getConstraintForce
  (JNIEnv *, jobject, jlong, jobject);

/*
 * Class:     com_jme3_bullet_MultiBodyLink
 * Method:    getConstraintTorque
 * Signature: (JLcom/jme3/math/Vector3f;)V
 */
JNIEXPORT void JNICALL Java_com_jme3_bullet_MultiBodyLink_getConstraintTorque
  (JNIEnv *, jobject, jlong, jobject);

/*
 * Class:     com_jme3_bullet_MultiBodyLink
 * Method:    getDofCount
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_jme3_bullet_MultiBodyLink_getDofCount
  (JNIEnv *, jobject, jlong);

/*
 * Class:     com_jme3_bullet_MultiBodyLink
 * Method:    getDVector
 * Signature: (JLcom/jme3/math/Vector3f;)V
 */
JNIEXPORT void JNICALL Java_com_jme3_bullet_MultiBodyLink_getDVector
  (JNIEnv *, jobject, jlong, jobject);

/*
 * Class:     com_jme3_bullet_MultiBodyLink
 * Method:    getEVector
 * Signature: (JLcom/jme3/math/Vector3f;)V
 */
JNIEXPORT void JNICALL Java_com_jme3_bullet_MultiBodyLink_getEVector
  (JNIEnv *, jobject, jlong, jobject);

/*
 * Class:     com_jme3_bullet_MultiBodyLink
 * Method:    getFlags
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_jme3_bullet_MultiBodyLink_getFlags
  (JNIEnv *, jobject, jlong);

/*
 * Class:     com_jme3_bullet_MultiBodyLink
 * Method:    getInertiaLocal
 * Signature: (JLcom/jme3/math/Vector3f;)V
 */
JNIEXPORT void JNICALL Java_com_jme3_bullet_MultiBodyLink_getInertiaLocal
  (JNIEnv *, jobject, jlong, jobject);

/*
 * Class:     com_jme3_bullet_MultiBodyLink
 * Method:    getJointPos
 * Signature: (JI)F
 */
JNIEXPORT jfloat JNICALL Java_com_jme3_bullet_MultiBodyLink_getJointPos
  (JNIEnv *, jobject, jlong, jint);

/*
 * Class:     com_jme3_bullet_MultiBodyLink
 * Method:    getJointTorque
 * Signature: (JI)F
 */
JNIEXPORT jfloat JNICALL Java_com_jme3_bullet_MultiBodyLink_getJointTorque
  (JNIEnv *, jobject, jlong, jint);

/*
 * Class:     com_jme3_bullet_MultiBodyLink
 * Method:    getJointType
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_jme3_bullet_MultiBodyLink_getJointType
  (JNIEnv *, jobject, jlong);

/*
 * Class:     com_jme3_bullet_MultiBodyLink
 * Method:    getJointVel
 * Signature: (JII)F
 */
JNIEXPORT jfloat JNICALL Java_com_jme3_bullet_MultiBodyLink_getJointVel
  (JNIEnv *, jobject, jlong, jint, jint);

/*
 * Class:     com_jme3_bullet_MultiBodyLink
 * Method:    getLinkId
 * Signature: (JI)J
 */
JNIEXPORT jlong JNICALL Java_com_jme3_bullet_MultiBodyLink_getLinkId
  (JNIEnv *, jobject, jlong, jint);

/*
 * Class:     com_jme3_bullet_MultiBodyLink
 * Method:    getMass
 * Signature: (J)F
 */
JNIEXPORT jfloat JNICALL Java_com_jme3_bullet_MultiBodyLink_getMass
  (JNIEnv *, jobject, jlong);

/*
 * Class:     com_jme3_bullet_MultiBodyLink
 * Method:    getParent2LinkRotation
 * Signature: (JLcom/jme3/math/Quaternion;)V
 */
JNIEXPORT void JNICALL Java_com_jme3_bullet_MultiBodyLink_getParent2LinkRotation
  (JNIEnv *, jobject, jlong, jobject);

/*
 * Class:     com_jme3_bullet_MultiBodyLink
 * Method:    getParentIndex
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_jme3_bullet_MultiBodyLink_getParentIndex
  (JNIEnv *, jobject, jlong);

/*
 * Class:     com_jme3_bullet_MultiBodyLink
 * Method:    getPosVarCount
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_jme3_bullet_MultiBodyLink_getPosVarCount
  (JNIEnv *, jobject, jlong);

/*
 * Class:     com_jme3_bullet_MultiBodyLink
 * Method:    getQ0Parent2LinkRotation
 * Signature: (JLcom/jme3/math/Quaternion;)V
 */
JNIEXPORT void JNICALL Java_com_jme3_bullet_MultiBodyLink_getQ0Parent2LinkRotation
  (JNIEnv *, jobject, jlong, jobject);

/*
 * Class:     com_jme3_bullet_MultiBodyLink
 * Method:    getWorldTransform
 * Signature: (JLcom/jme3/math/Transform;)V
 */
JNIEXPORT void JNICALL Java_com_jme3_bullet_MultiBodyLink_getWorldTransform
  (JNIEnv *, jobject, jlong, jobject);

/*
 * Class:     com_jme3_bullet_MultiBodyLink
 * Method:    localFrameToWorld
 * Signature: (JILcom/jme3/math/Matrix3f;)V
 */
JNIEXPORT void JNICALL Java_com_jme3_bullet_MultiBodyLink_localFrameToWorld
  (JNIEnv *, jobject, jlong, jint, jobject);

/*
 * Class:     com_jme3_bullet_MultiBodyLink
 * Method:    localPosToWorld
 * Signature: (JILcom/jme3/math/Vector3f;)V
 */
JNIEXPORT void JNICALL Java_com_jme3_bullet_MultiBodyLink_localPosToWorld
  (JNIEnv *, jobject, jlong, jint, jobject);

/*
 * Class:     com_jme3_bullet_MultiBodyLink
 * Method:    setCollider
 * Signature: (JJ)V
 */
JNIEXPORT void JNICALL Java_com_jme3_bullet_MultiBodyLink_setCollider
  (JNIEnv *, jobject, jlong, jlong);

/*
 * Class:     com_jme3_bullet_MultiBodyLink
 * Method:    setJointPos
 * Signature: (JIIF)V
 */
JNIEXPORT void JNICALL Java_com_jme3_bullet_MultiBodyLink_setJointPos
  (JNIEnv *, jobject, jlong, jint, jint, jfloat);

/*
 * Class:     com_jme3_bullet_MultiBodyLink
 * Method:    setJointVel
 * Signature: (JIIF)V
 */
JNIEXPORT void JNICALL Java_com_jme3_bullet_MultiBodyLink_setJointVel
  (JNIEnv *, jobject, jlong, jint, jint, jfloat);

/*
 * Class:     com_jme3_bullet_MultiBodyLink
 * Method:    worldPosToLocal
 * Signature: (JILcom/jme3/math/Vector3f;)V
 */
JNIEXPORT void JNICALL Java_com_jme3_bullet_MultiBodyLink_worldPosToLocal
  (JNIEnv *, jobject, jlong, jint, jobject);

#ifdef __cplusplus
}
#endif
#endif