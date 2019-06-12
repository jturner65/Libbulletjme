/*
 * Copyright (c) 2009-2015 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
 * Author: Dokthar
 */
#include "com_jme3_bullet_objects_infos_SoftBodyConfig.h"
#include "jmeBulletUtil.h"
#include "BulletSoftBody/btSoftBody.h"
#include "BulletSoftBody/btSoftBodyHelpers.h"

#ifdef __cplusplus
extern "C" {
#endif

    /*
     * Class:     com_jme3_bullet_objects_infos_SoftBodyConfig
     * Method:    copyValues
     * Signature: (JJ)V
     */
    JNIEXPORT void JNICALL Java_com_jme3_bullet_objects_infos_SoftBodyConfig_copyValues
    (JNIEnv *env, jobject object, jlong thisId, jlong bodyId) {
        btSoftBody* body = reinterpret_cast<btSoftBody*> (thisId);
        NULL_CHECK(body, "The target btSoftBody does not exist.",)

        btSoftBody* other = reinterpret_cast<btSoftBody*> (bodyId);
        NULL_CHECK(other, "The source btSoftBody does not exist.",)

        body->m_cfg.kVCF = other->m_cfg.kVCF; // Velocities correction factor (Baumgarte)
        body->m_cfg.kDP = other->m_cfg.kDP; // Damping coefficient [0,1]
        body->m_cfg.kDG = other->m_cfg.kDG; // Drag coefficient [0,+inf]
        body->m_cfg.kLF = other->m_cfg.kLF; // Lift coefficient [0,+inf]
        body->m_cfg.kPR = other->m_cfg.kPR; // Pressure coefficient [-inf,+inf]
        body->m_cfg.kVC = other->m_cfg.kVC; // Volume conversation coefficient [0,+inf]
        body->m_cfg.kDF = other->m_cfg.kDF; // Dynamic friction coefficient [0,1]
        body->m_cfg.kMT = other->m_cfg.kMT; // Pose matching coefficient [0,1]

        body->m_cfg.kCHR = other->m_cfg.kCHR; // Rigid contacts hardness [0,1]
        body->m_cfg.kKHR = other->m_cfg.kKHR; // Kinetic contacts hardness [0,1]
        body->m_cfg.kSHR = other->m_cfg.kSHR; // Soft contacts hardness [0,1]
        body->m_cfg.kAHR = other->m_cfg.kAHR; // Anchors hardness [0,1]

        body->m_cfg.kSRHR_CL = other->m_cfg.kSRHR_CL; // Soft vs rigid hardness [0,1] (cluster only)
        body->m_cfg.kSKHR_CL = other->m_cfg.kSKHR_CL; // Soft vs kinetic hardness [0,1] (cluster only)
        body->m_cfg.kSSHR_CL = other->m_cfg.kSSHR_CL; // Soft vs soft hardness [0,1] (cluster only)
        body->m_cfg.kSR_SPLT_CL = other->m_cfg.kSR_SPLT_CL; // Soft vs rigid impulse split [0,1] (cluster only)
        body->m_cfg.kSK_SPLT_CL = other->m_cfg.kSK_SPLT_CL; // Soft vs kinetic impulse split [0,1] (cluster only)
        body->m_cfg.kSS_SPLT_CL = other->m_cfg.kSS_SPLT_CL; // Soft vs soft impulse split [0,1] (cluster only)

        body->m_cfg.maxvolume = other->m_cfg.maxvolume; // Maximum volume ratio for pose
        body->m_cfg.timescale = other->m_cfg.timescale; // Time scale

        body->m_cfg.viterations = other->m_cfg.viterations; // Velocities solver iterations
        body->m_cfg.piterations = other->m_cfg.piterations; // Positions solver iterations
        body->m_cfg.diterations = other->m_cfg.diterations; // Drift solver iterations
        body->m_cfg.citerations = other->m_cfg.citerations; // Cluster solver iterations

        body->m_cfg.collisions = other->m_cfg.collisions; // Collisions flags
    }

    /*
     * Class:     com_jme3_bullet_objects_infos_SoftBodyConfig
     * Method:    getAeroModel
     * Signature: (J)I
     */
    JNIEXPORT jint JNICALL Java_com_jme3_bullet_objects_infos_SoftBodyConfig_getAeroModel
    (JNIEnv *env, jobject object, jlong bodyId) {
        btSoftBody* body = reinterpret_cast<btSoftBody*> (bodyId);
        NULL_CHECK(body, "The btSoftBody does not exist.", 0)

        return body->m_cfg.aeromodel;
    }

    /*
     * Class:     com_jme3_bullet_objects_infos_SoftBodyConfig
     * Method:    getAnchorsHardness
     * Signature: (J)F
     */
    JNIEXPORT jfloat JNICALL Java_com_jme3_bullet_objects_infos_SoftBodyConfig_getAnchorsHardness
    (JNIEnv *env, jobject object, jlong bodyId) {
        btSoftBody* body = reinterpret_cast<btSoftBody*> (bodyId);
        NULL_CHECK(body, "The btSoftBody does not exist.", 0)

        return body->m_cfg.kAHR;
    }

    /*
     * Class:     com_jme3_bullet_objects_infos_SoftBodyConfig
     * Method:    getClusterIterations
     * Signature: (J)I
     */
    JNIEXPORT jint JNICALL Java_com_jme3_bullet_objects_infos_SoftBodyConfig_getClusterIterations
    (JNIEnv *env, jobject object, jlong bodyId) {
        btSoftBody* body = reinterpret_cast<btSoftBody*> (bodyId);
        NULL_CHECK(body, "The btSoftBody does not exist.", 0)

        return body->m_cfg.citerations;
    }

    /*
     * Class:     com_jme3_bullet_objects_infos_SoftBodyConfig
     * Method:    getClusterKineticHardness
     * Signature: (J)F
     */
    JNIEXPORT jfloat JNICALL Java_com_jme3_bullet_objects_infos_SoftBodyConfig_getClusterKineticHardness
    (JNIEnv *env, jobject object, jlong bodyId) {
        btSoftBody* body = reinterpret_cast<btSoftBody*> (bodyId);
        NULL_CHECK(body, "The btSoftBody does not exist.", 0)

        return body->m_cfg.kSKHR_CL;
    }

    /*
     * Class:     com_jme3_bullet_objects_infos_SoftBodyConfig
     * Method:    getClusterKineticImpulseSplitCoef
     * Signature: (J)F
     */
    JNIEXPORT jfloat JNICALL Java_com_jme3_bullet_objects_infos_SoftBodyConfig_getClusterKineticImpulseSplitCoef
    (JNIEnv *env, jobject object, jlong bodyId) {
        btSoftBody* body = reinterpret_cast<btSoftBody*> (bodyId);
        NULL_CHECK(body, "The btSoftBody does not exist.", 0)

        return body->m_cfg.kSK_SPLT_CL;
    }

    /*
     * Class:     com_jme3_bullet_objects_infos_SoftBodyConfig
     * Method:    getClusterRigidHardness
     * Signature: (J)F
     */
    JNIEXPORT jfloat JNICALL Java_com_jme3_bullet_objects_infos_SoftBodyConfig_getClusterRigidHardness
    (JNIEnv *env, jobject object, jlong bodyId) {
        btSoftBody* body = reinterpret_cast<btSoftBody*> (bodyId);
        NULL_CHECK(body, "The btSoftBody does not exist.", 0)

        return body->m_cfg.kSRHR_CL;
    }

    /*
     * Class:     com_jme3_bullet_objects_infos_SoftBodyConfig
     * Method:    getClusterRigidImpulseSplitCoef
     * Signature: (J)F
     */
    JNIEXPORT jfloat JNICALL Java_com_jme3_bullet_objects_infos_SoftBodyConfig_getClusterRigidImpulseSplitCoef
    (JNIEnv *env, jobject object, jlong bodyId) {
        btSoftBody* body = reinterpret_cast<btSoftBody*> (bodyId);
        NULL_CHECK(body, "The btSoftBody does not exist.", 0)

        return body->m_cfg.kSR_SPLT_CL;
    }

    /*
     * Class:     com_jme3_bullet_objects_objects_infos_SoftBodyConfig
     * Method:    getClusterSoftHardness
     * Signature: (J)F
     */
    JNIEXPORT jfloat JNICALL Java_com_jme3_bullet_objects_infos_SoftBodyConfig_getClusterSoftHardness
    (JNIEnv *env, jobject object, jlong bodyId) {
        btSoftBody* body = reinterpret_cast<btSoftBody*> (bodyId);
        NULL_CHECK(body, "The btSoftBody does not exist.", 0)

        return body->m_cfg.kSSHR_CL;
    }

    /*
     * Class:     com_jme3_bullet_objects_infos_SoftBodyConfig
     * Method:    getClusterSoftImpulseSplitCoef
     * Signature: (J)F
     */
    JNIEXPORT jfloat JNICALL Java_com_jme3_bullet_objects_infos_SoftBodyConfig_getClusterSoftImpulseSplitCoef
    (JNIEnv *env, jobject object, jlong bodyId) {
        btSoftBody* body = reinterpret_cast<btSoftBody*> (bodyId);
        NULL_CHECK(body, "The btSoftBody does not exist.", 0)

        return body->m_cfg.kSS_SPLT_CL;
    }

    /*
     * Class:     com_jme3_bullet_objects_infos_SoftBodyConfig
     * Method:    getCollisionsFlags
     * Signature: (J)I
     */
    JNIEXPORT jint JNICALL Java_com_jme3_bullet_objects_infos_SoftBodyConfig_getCollisionsFlags
    (JNIEnv *env, jobject object, jlong bodyId) {
        btSoftBody* body = reinterpret_cast<btSoftBody*> (bodyId);
        NULL_CHECK(body, "The btSoftBody does not exist.", 0)

        return body->m_cfg.collisions;
    }

    /*
     * Class:     com_jme3_bullet_objects_infos_SoftBodyConfig
     * Method:    getDampingCoef
     * Signature: (J)F
     */
    JNIEXPORT jfloat JNICALL Java_com_jme3_bullet_objects_infos_SoftBodyConfig_getDampingCoef
    (JNIEnv *env, jobject object, jlong bodyId) {
        btSoftBody* body = reinterpret_cast<btSoftBody*> (bodyId);
        NULL_CHECK(body, "The btSoftBody does not exist.", 0)

        return body->m_cfg.kDP;
    }

    /*
     * Class:     com_jme3_bullet_objects_infos_SoftBodyConfig
     * Method:    getDragCoef
     * Signature: (J)F
     */
    JNIEXPORT jfloat JNICALL Java_com_jme3_bullet_objects_infos_SoftBodyConfig_getDragCoef
    (JNIEnv *env, jobject object, jlong bodyId) {
        btSoftBody* body = reinterpret_cast<btSoftBody*> (bodyId);
        NULL_CHECK(body, "The btSoftBody does not exist.", 0)

        return body->m_cfg.kDG;
    }

    /*
     * Class:     com_jme3_bullet_objects_infos_SoftBodyConfig
     * Method:    getDriftIterations
     * Signature: (J)I
     */
    JNIEXPORT jint JNICALL Java_com_jme3_bullet_objects_infos_SoftBodyConfig_getDriftIterations
    (JNIEnv *env, jobject object, jlong bodyId) {
        btSoftBody* body = reinterpret_cast<btSoftBody*> (bodyId);
        NULL_CHECK(body, "The btSoftBody does not exist.", 0)

        return body->m_cfg.diterations;
    }

    /*
     * Class:     com_jme3_bullet_objects_infos_SoftBodyConfig
     * Method:    getDynamicFrictionCoef
     * Signature: (J)F
     */
    JNIEXPORT jfloat JNICALL Java_com_jme3_bullet_objects_infos_SoftBodyConfig_getDynamicFrictionCoef
    (JNIEnv *env, jobject object, jlong bodyId) {
        btSoftBody* body = reinterpret_cast<btSoftBody*> (bodyId);
        NULL_CHECK(body, "The btSoftBody does not exist.", 0)

        return body->m_cfg.kDF;
    }

    /*
     * Class:     com_jme3_bullet_objects_infos_SoftBodyConfig
     * Method:    getKineticContactsHardness
     * Signature: (J)F
     */
    JNIEXPORT jfloat JNICALL Java_com_jme3_bullet_objects_infos_SoftBodyConfig_getKineticContactsHardness
    (JNIEnv *env, jobject object, jlong bodyId) {
        btSoftBody* body = reinterpret_cast<btSoftBody*> (bodyId);
        NULL_CHECK(body, "The btSoftBody does not exist.", 0)

        return body->m_cfg.kKHR;
    }

    /*
     * Class:     com_jme3_bullet_objects_infos_SoftBodyConfig
     * Method:    getLiftCoef
     * Signature: (J)F
     */
    JNIEXPORT jfloat JNICALL Java_com_jme3_bullet_objects_infos_SoftBodyConfig_getLiftCoef
    (JNIEnv *env, jobject object, jlong bodyId) {
        btSoftBody* body = reinterpret_cast<btSoftBody*> (bodyId);
        NULL_CHECK(body, "The btSoftBody does not exist.", 0)

        return body->m_cfg.kLF;
    }

    /*
     * Class:     com_jme3_bullet_objects_infos_SoftBodyConfig
     * Method:    getMaximumVolumeRatio
     * Signature: (J)F
     */
    JNIEXPORT jfloat JNICALL Java_com_jme3_bullet_objects_infos_SoftBodyConfig_getMaximumVolumeRatio
    (JNIEnv *env, jobject object, jlong bodyId) {
        btSoftBody* body = reinterpret_cast<btSoftBody*> (bodyId);
        NULL_CHECK(body, "The btSoftBody does not exist.", 0)

        return body->m_cfg.maxvolume;
    }

    /*
     * Class:     com_jme3_bullet_objects_infos_SoftBodyConfig
     * Method:    getPoseMatchingCoef
     * Signature: (J)F
     */
    JNIEXPORT jfloat JNICALL Java_com_jme3_bullet_objects_infos_SoftBodyConfig_getPoseMatchingCoef
    (JNIEnv *env, jobject object, jlong bodyId) {
        btSoftBody* body = reinterpret_cast<btSoftBody*> (bodyId);
        NULL_CHECK(body, "The btSoftBody does not exist.", 0)

        return body->m_cfg.kMT;
    }

    /*
     * Class:     com_jme3_bullet_objects_infos_SoftBodyConfig
     * Method:    getPositionIterations
     * Signature: (J)I
     */
    JNIEXPORT jint JNICALL Java_com_jme3_bullet_objects_infos_SoftBodyConfig_getPositionIterations
    (JNIEnv *env, jobject object, jlong bodyId) {
        btSoftBody* body = reinterpret_cast<btSoftBody*> (bodyId);
        NULL_CHECK(body, "The btSoftBody does not exist.", 0)

        return body->m_cfg.piterations;
    }

    /*
     * Class:     com_jme3_bullet_objects_infos_SoftBodyConfig
     * Method:    getPressureCoef
     * Signature: (J)F
     */
    JNIEXPORT jfloat JNICALL Java_com_jme3_bullet_objects_infos_SoftBodyConfig_getPressureCoef
    (JNIEnv *env, jobject object, jlong bodyId) {
        btSoftBody* body = reinterpret_cast<btSoftBody*> (bodyId);
        NULL_CHECK(body, "The btSoftBody does not exist.", 0)

        return body->m_cfg.kPR;
    }

    /*
     * Class:     com_jme3_bullet_objects_infos_SoftBodyConfig
     * Method:    getRigidContactsHardness
     * Signature: (J)F
     */
    JNIEXPORT jfloat JNICALL Java_com_jme3_bullet_objects_infos_SoftBodyConfig_getRigidContactsHardness
    (JNIEnv *env, jobject object, jlong bodyId) {
        btSoftBody* body = reinterpret_cast<btSoftBody*> (bodyId);
        NULL_CHECK(body, "The btSoftBody does not exist.", 0)

        return body->m_cfg.kCHR;
    }

    /*
     * Class:     com_jme3_bullet_objects_infos_SoftBodyConfig
     * Method:    getSoftContactsHardness
     * Signature: (J)F
     */
    JNIEXPORT jfloat JNICALL Java_com_jme3_bullet_objects_infos_SoftBodyConfig_getSoftContactsHardness
    (JNIEnv *env, jobject object, jlong bodyId) {
        btSoftBody* body = reinterpret_cast<btSoftBody*> (bodyId);
        NULL_CHECK(body, "The btSoftBody does not exist.", 0)

        return body->m_cfg.kSHR;
    }

    /*
     * Class:     com_jme3_bullet_objects_infos_SoftBodyConfig
     * Method:    getTimeScale
     * Signature: (J)F
     */
    JNIEXPORT jfloat JNICALL Java_com_jme3_bullet_objects_infos_SoftBodyConfig_getTimeScale
    (JNIEnv *env, jobject object, jlong bodyId) {
        btSoftBody* body = reinterpret_cast<btSoftBody*> (bodyId);
        NULL_CHECK(body, "The btSoftBody does not exist.", 0)

        return body->m_cfg.timescale;
    }

    /*
     * Class:     com_jme3_bullet_objects_infos_SoftBodyConfig
     * Method:    getVelocitiesCorrectionFactor
     * Signature: (J)F
     */
    JNIEXPORT jfloat JNICALL Java_com_jme3_bullet_objects_infos_SoftBodyConfig_getVelocitiesCorrectionFactor
    (JNIEnv *env, jobject object, jlong bodyId) {
        btSoftBody* body = reinterpret_cast<btSoftBody*> (bodyId);
        NULL_CHECK(body, "The btSoftBody does not exist.", 0)

        return body->m_cfg.kVCF;
    }

    /*
     * Class:     com_jme3_bullet_objects_infos_SoftBodyConfig
     * Method:    getVelocitiesIterations
     * Signature: (J)I
     */
    JNIEXPORT jint JNICALL Java_com_jme3_bullet_objects_infos_SoftBodyConfig_getVelocitiesIterations
    (JNIEnv *env, jobject object, jlong bodyId) {
        btSoftBody* body = reinterpret_cast<btSoftBody*> (bodyId);
        NULL_CHECK(body, "The btSoftBody does not exist.", 0)

        return body->m_cfg.viterations;
    }

    /*
     * Class:     com_jme3_bullet_objects_infos_SoftBodyConfig
     * Method:    getVolumeConservationCoef
     * Signature: (J)F
     */
    JNIEXPORT jfloat JNICALL Java_com_jme3_bullet_objects_infos_SoftBodyConfig_getVolumeConservationCoef
    (JNIEnv *env, jobject object, jlong bodyId) {
        btSoftBody* body = reinterpret_cast<btSoftBody*> (bodyId);
        NULL_CHECK(body, "The btSoftBody does not exist.", 0)

        return body->m_cfg.kVC;
    }

    /*
     * Class:     com_jme3_bullet_objects_infos_SoftBodyConfig
     * Method:    setAeroModel
     * Signature: (JI)V
     */
    JNIEXPORT void JNICALL Java_com_jme3_bullet_objects_infos_SoftBodyConfig_setAeroModel
    (JNIEnv *env, jobject object, jlong bodyId, jint model) {
        btSoftBody* body = reinterpret_cast<btSoftBody*> (bodyId);
        NULL_CHECK(body, "The btSoftBody does not exist.",)

        body->m_cfg.aeromodel = (btSoftBody::eAeroModel::_) model;
    }

    /*
     * Class:     com_jme3_bullet_objects_infos_SoftBodyConfig
     * Method:    setAnchorsHardness
     * Signature: (JF)V
     */
    JNIEXPORT void JNICALL Java_com_jme3_bullet_objects_infos_SoftBodyConfig_setAnchorsHardness
    (JNIEnv *env, jobject object, jlong bodyId, jfloat coef) {
        btSoftBody* body = reinterpret_cast<btSoftBody*> (bodyId);
        NULL_CHECK(body, "The btSoftBody does not exist.",)

        body->m_cfg.kAHR = coef;
    }

    /*
     * Class:     com_jme3_bullet_objects_infos_SoftBodyConfig
     * Method:    setClusterIterations
     * Signature: (JI)V
     */
    JNIEXPORT void JNICALL Java_com_jme3_bullet_objects_infos_SoftBodyConfig_setClusterIterations
    (JNIEnv *env, jobject object, jlong bodyId, jint iter) {
        btSoftBody* body = reinterpret_cast<btSoftBody*> (bodyId);
        NULL_CHECK(body, "The btSoftBody does not exist.",)

        body->m_cfg.citerations = iter;
    }

    /*
     * Class:     com_jme3_bullet_objects_infos_SoftBodyConfig
     * Method:    setClusterKineticHardness
     * Signature: (JF)V
     */
    JNIEXPORT void JNICALL Java_com_jme3_bullet_objects_infos_SoftBodyConfig_setClusterKineticHardness
    (JNIEnv *env, jobject object, jlong bodyId, jfloat coef) {
        btSoftBody* body = reinterpret_cast<btSoftBody*> (bodyId);
        NULL_CHECK(body, "The btSoftBody does not exist.",)

        body->m_cfg.kSKHR_CL = coef;
    }

    /*
     * Class:     com_jme3_bullet_objects_infos_SoftBodyConfig
     * Method:    setClusterKineticImpulseSplitCoef
     * Signature: (JF)V
     */
    JNIEXPORT void JNICALL Java_com_jme3_bullet_objects_infos_SoftBodyConfig_setClusterKineticImpulseSplitCoef
    (JNIEnv *env, jobject object, jlong bodyId, jfloat coef) {
        btSoftBody* body = reinterpret_cast<btSoftBody*> (bodyId);
        NULL_CHECK(body, "The btSoftBody does not exist.",)

        body->m_cfg.kSK_SPLT_CL = coef;
    }

    /*
     * Class:     com_jme3_bullet_objects_infos_SoftBodyConfig
     * Method:    setClusterRigidHardness
     * Signature: (JF)V
     */
    JNIEXPORT void JNICALL Java_com_jme3_bullet_objects_infos_SoftBodyConfig_setClusterRigidHardness
    (JNIEnv *env, jobject object, jlong bodyId, jfloat coef) {
        btSoftBody* body = reinterpret_cast<btSoftBody*> (bodyId);
        NULL_CHECK(body, "The btSoftBody does not exist.",)

        body->m_cfg.kSRHR_CL = coef;
    }

    /*
     * Class:     com_jme3_bullet_objects_infos_SoftBodyConfig
     * Method:    setClusterRigidImpulseSplitCoef
     * Signature: (JF)V
     */
    JNIEXPORT void JNICALL Java_com_jme3_bullet_objects_infos_SoftBodyConfig_setClusterRigidImpulseSplitCoef
    (JNIEnv *env, jobject object, jlong bodyId, jfloat coef) {
        btSoftBody* body = reinterpret_cast<btSoftBody*> (bodyId);
        NULL_CHECK(body, "The btSoftBody does not exist.",)

        body->m_cfg.kSR_SPLT_CL = coef;
    }

    /*
     * Class:     com_jme3_bullet_objects_infos_SoftBodyConfig
     * Method:    setClusterSoftHardness
     * Signature: (JF)V
     */
    JNIEXPORT void JNICALL Java_com_jme3_bullet_objects_infos_SoftBodyConfig_setClusterSoftHardness
    (JNIEnv *env, jobject object, jlong bodyId, jfloat coef) {
        btSoftBody* body = reinterpret_cast<btSoftBody*> (bodyId);
        NULL_CHECK(body, "The btSoftBody does not exist.",)

        body->m_cfg.kSSHR_CL = coef;
    }

    /*
     * Class:     com_jme3_bullet_objects_infos_SoftBodyConfig
     * Method:    setClusterSoftImpulseSplitCoef
     * Signature: (JF)V
     */
    JNIEXPORT void JNICALL Java_com_jme3_bullet_objects_infos_SoftBodyConfig_setClusterSoftImpulseSplitCoef
    (JNIEnv *env, jobject object, jlong bodyId, jfloat coef) {
        btSoftBody* body = reinterpret_cast<btSoftBody*> (bodyId);
        NULL_CHECK(body, "The btSoftBody does not exist.",)

        body->m_cfg.kSS_SPLT_CL = coef;
    }

    /*
     * Class:     com_jme3_bullet_objects_infos_SoftBodyConfig
     * Method:    setCollisionsFlags
     * Signature: (JI)V
     */
    JNIEXPORT void JNICALL Java_com_jme3_bullet_objects_infos_SoftBodyConfig_setCollisionsFlags
    (JNIEnv *env, jobject object, jlong bodyId, jint flags) {
        btSoftBody* body = reinterpret_cast<btSoftBody*> (bodyId);
        NULL_CHECK(body, "The btSoftBody does not exist.",)

        body->m_cfg.collisions = flags;
    }

    /*
     * Class:     com_jme3_bullet_objects_infos_SoftBodyConfig
     * Method:    setDampingCoef
     * Signature: (JF)V
     */
    JNIEXPORT void JNICALL Java_com_jme3_bullet_objects_infos_SoftBodyConfig_setDampingCoef
    (JNIEnv *env, jobject object, jlong bodyId, jfloat ceof) {
        btSoftBody* body = reinterpret_cast<btSoftBody*> (bodyId);
        NULL_CHECK(body, "The btSoftBody does not exist.",)

        body->m_cfg.kDP = ceof;
    }

    /*
     * Class:     com_jme3_bullet_objects_infos_SoftBodyConfig
     * Method:    setDragCoef
     * Signature: (JF)V
     */
    JNIEXPORT void JNICALL Java_com_jme3_bullet_objects_infos_SoftBodyConfig_setDragCoef
    (JNIEnv *env, jobject object, jlong bodyId, jfloat coef) {
        btSoftBody* body = reinterpret_cast<btSoftBody*> (bodyId);
        NULL_CHECK(body, "The btSoftBody does not exist.",)

        body->m_cfg.kDG = coef;
    }

    /*
     * Class:     com_jme3_bullet_objects_infos_SoftBodyConfig
     * Method:    setDriftIterations
     * Signature: (JI)V
     */
    JNIEXPORT void JNICALL Java_com_jme3_bullet_objects_infos_SoftBodyConfig_setDriftIterations
    (JNIEnv *env, jobject object, jlong bodyId, jint iter) {
        btSoftBody* body = reinterpret_cast<btSoftBody*> (bodyId);
        NULL_CHECK(body, "The btSoftBody does not exist.",)

        body->m_cfg.diterations = iter;
    }

    /*
     * Class:     com_jme3_bullet_objects_infos_SoftBodyConfig
     * Method:    setDynamicFrictionCoef
     * Signature: (JF)V
     */
    JNIEXPORT void JNICALL Java_com_jme3_bullet_objects_infos_SoftBodyConfig_setDynamicFrictionCoef
    (JNIEnv *env, jobject object, jlong bodyId, jfloat coef) {
        btSoftBody* body = reinterpret_cast<btSoftBody*> (bodyId);
        NULL_CHECK(body, "The btSoftBody does not exist.",)

        body->m_cfg.kDF = coef;
    }

    /*
     * Class:     com_jme3_bullet_objects_infos_SoftBodyConfig
     * Method:    setKineticContactsHardness
     * Signature: (JF)V
     */
    JNIEXPORT void JNICALL Java_com_jme3_bullet_objects_infos_SoftBodyConfig_setKineticContactsHardness
    (JNIEnv *env, jobject object, jlong bodyId, jfloat coef) {
        btSoftBody* body = reinterpret_cast<btSoftBody*> (bodyId);
        NULL_CHECK(body, "The btSoftBody does not exist.",)

        body->m_cfg.kKHR = coef;
    }

    /*
     * Class:     com_jme3_bullet_objects_infos_SoftBodyConfig
     * Method:    setLiftCoef
     * Signature: (JF)V
     */
    JNIEXPORT void JNICALL Java_com_jme3_bullet_objects_infos_SoftBodyConfig_setLiftCoef
    (JNIEnv *env, jobject object, jlong bodyId, jfloat coef) {
        btSoftBody* body = reinterpret_cast<btSoftBody*> (bodyId);
        NULL_CHECK(body, "The btSoftBody does not exist.",)

        body->m_cfg.kLF = coef;
    }

    /*
     * Class:     com_jme3_bullet_objects_infos_SoftBodyConfig
     * Method:    setMaximumVolumeRatio
     * Signature: (JF)V
     */
    JNIEXPORT void JNICALL Java_com_jme3_bullet_objects_infos_SoftBodyConfig_setMaximumVolumeRatio
    (JNIEnv *env, jobject object, jlong bodyId, jfloat ratio) {
        btSoftBody* body = reinterpret_cast<btSoftBody*> (bodyId);
        NULL_CHECK(body, "The btSoftBody does not exist.",)

        body->m_cfg.maxvolume = ratio;
    }

    /*
     * Class:     com_jme3_bullet_objects_infos_SoftBodyConfig
     * Method:    setPoseMatchingCoef
     * Signature: (JF)V
     */
    JNIEXPORT void JNICALL Java_com_jme3_bullet_objects_infos_SoftBodyConfig_setPoseMatchingCoef
    (JNIEnv *env, jobject object, jlong bodyId, jfloat coef) {
        btSoftBody* body = reinterpret_cast<btSoftBody*> (bodyId);
        NULL_CHECK(body, "The btSoftBody does not exist.",)

        body->m_cfg.kMT = coef;
    }

    /*
     * Class:     com_jme3_bullet_objects_infos_SoftBodyConfig
     * Method:    setPositionIterations
     * Signature: (JI)V
     */
    JNIEXPORT void JNICALL Java_com_jme3_bullet_objects_infos_SoftBodyConfig_setPositionIterations
    (JNIEnv *env, jobject object, jlong bodyId, jint iter) {
        btSoftBody* body = reinterpret_cast<btSoftBody*> (bodyId);
        NULL_CHECK(body, "The btSoftBody does not exist.",)

        body->m_cfg.piterations = iter;
    }

    /*
     * Class:     com_jme3_bullet_objects_infos_SoftBodyConfig
     * Method:    setPressureCoef
     * Signature: (JF)V
     */
    JNIEXPORT void JNICALL Java_com_jme3_bullet_objects_infos_SoftBodyConfig_setPressureCoef
    (JNIEnv *env, jobject object, jlong bodyId, jfloat coef) {
        btSoftBody* body = reinterpret_cast<btSoftBody*> (bodyId);
        NULL_CHECK(body, "The btSoftBody does not exist.",)

        body->m_cfg.kPR = coef;
    }

    /*
     * Class:     com_jme3_bullet_objects_infos_SoftBodyConfig
     * Method:    setRigidContactsHardness
     * Signature: (JF)V
     */
    JNIEXPORT void JNICALL Java_com_jme3_bullet_objects_infos_SoftBodyConfig_setRigidContactsHardness
    (JNIEnv *env, jobject object, jlong bodyId, jfloat coef) {
        btSoftBody* body = reinterpret_cast<btSoftBody*> (bodyId);
        NULL_CHECK(body, "The btSoftBody does not exist.",)

        body->m_cfg.kCHR = coef;
    }

    /*
     * Class:     com_jme3_bullet_objects_infos_SoftBodyConfig
     * Method:    setSoftContactsHardness
     * Signature: (JF)V
     */
    JNIEXPORT void JNICALL Java_com_jme3_bullet_objects_infos_SoftBodyConfig_setSoftContactsHardness
    (JNIEnv *env, jobject object, jlong bodyId, jfloat coef) {
        btSoftBody* body = reinterpret_cast<btSoftBody*> (bodyId);
        NULL_CHECK(body, "The btSoftBody does not exist.",)

        body->m_cfg.kSHR = coef;
    }

    /*
     * Class:     com_jme3_bullet_objects_infos_SoftBodyConfig
     * Method:    setTimeScale
     * Signature: (JF)V
     */
    JNIEXPORT void JNICALL Java_com_jme3_bullet_objects_infos_SoftBodyConfig_setTimeScale
    (JNIEnv *env, jobject object, jlong bodyId, jfloat scale) {
        btSoftBody* body = reinterpret_cast<btSoftBody*> (bodyId);
        NULL_CHECK(body, "The btSoftBody does not exist.",)

        body->m_cfg.timescale = scale;
    }

    /*
     * Class:     com_jme3_bullet_objects_infos_SoftBodyConfig
     * Method:    setVelocitiesCorrectionFactor
     * Signature: (JF)V
     */
    JNIEXPORT void JNICALL Java_com_jme3_bullet_objects_infos_SoftBodyConfig_setVelocitiesCorrectionFactor
    (JNIEnv *env, jobject object, jlong bodyId, jfloat factor) {
        btSoftBody* body = reinterpret_cast<btSoftBody*> (bodyId);
        NULL_CHECK(body, "The btSoftBody does not exist.",)

        body->m_cfg.kVCF = factor;
    }

    /*
     * Class:     com_jme3_bullet_objects_infos_SoftBodyConfig
     * Method:    setVelocitiesIterations
     * Signature: (JI)V
     */
    JNIEXPORT void JNICALL Java_com_jme3_bullet_objects_infos_SoftBodyConfig_setVelocitiesIterations
    (JNIEnv *env, jobject object, jlong bodyId, jint iter) {
        btSoftBody* body = reinterpret_cast<btSoftBody*> (bodyId);
        NULL_CHECK(body, "The btSoftBody does not exist.",)

        body->m_cfg.viterations = iter;
    }

    /*
     * Class:     com_jme3_bullet_objects_infos_SoftBodyConfig
     * Method:    setVolumeConservationCoef
     * Signature: (JF)V
     */
    JNIEXPORT void JNICALL Java_com_jme3_bullet_objects_infos_SoftBodyConfig_setVolumeConservationCoef
    (JNIEnv *env, jobject object, jlong bodyId, jfloat coef) {
        btSoftBody* body = reinterpret_cast<btSoftBody*> (bodyId);
        NULL_CHECK(body, "The btSoftBody does not exist.",)

        body->m_cfg.kVC = coef;
    }

#ifdef __cplusplus
}
#endif