/*
 Copyright (c) 2020, Stephen Gold
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright
 notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 notice, this list of conditions and the following disclaimer in the
 documentation and/or other materials provided with the distribution.
 * Neither the name of the copyright holder nor the names of its contributors
 may be used to endorse or promote products derived from this software without
 specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import com.jme3.bullet.CollisionSpace;
import com.jme3.bullet.MultiBody;
import com.jme3.bullet.MultiBodyJointType;
import com.jme3.bullet.MultiBodyLink;
import com.jme3.bullet.MultiBodySpace;
import com.jme3.bullet.PhysicsSoftSpace;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.RayTestFlag;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.PhysicsRayTestResult;
import com.jme3.bullet.collision.shapes.Box2dShape;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.CompoundCollisionShape;
import com.jme3.bullet.collision.shapes.ConeCollisionShape;
import com.jme3.bullet.collision.shapes.Convex2dShape;
import com.jme3.bullet.collision.shapes.CylinderCollisionShape;
import com.jme3.bullet.collision.shapes.EmptyShape;
import com.jme3.bullet.collision.shapes.HeightfieldCollisionShape;
import com.jme3.bullet.collision.shapes.HullCollisionShape;
import com.jme3.bullet.collision.shapes.MultiSphere;
import com.jme3.bullet.collision.shapes.PlaneCollisionShape;
import com.jme3.bullet.collision.shapes.SimplexCollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.objects.PhysicsBody;
import com.jme3.bullet.objects.PhysicsGhostObject;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.bullet.util.DebugShapeFactory;
import com.jme3.bullet.util.NativeLibrary;
import com.jme3.math.Plane;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.system.NativeLibraryLoader;
import java.io.File;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import vhacd.VHACD;
import vhacd.VHACDHull;
import vhacd.VHACDParameters;

/**
 * JUnit automated tests for Libbulletjme.
 *
 * @author Stephen Gold sgold@sonic.net
 */
public class TestLibbulletjme {

    @Test
    public void test001() {
        loadNativeLibrary();
        /*
         * Create a PhysicsSpace using DBVT for broadphase.
         */
        PhysicsSpace space = new PhysicsSpace(PhysicsSpace.BroadphaseType.DBVT);
        /*
         * Add a static horizontal plane at y=-1.
         */
        Plane plane = new Plane(Vector3f.UNIT_Y, -1f);
        CollisionShape pcs = new PlaneCollisionShape(plane);
        PhysicsRigidBody floorPrb = new PhysicsRigidBody(pcs, 0f);
        space.addCollisionObject(floorPrb);
        /*
         * Add a box-shaped dynamic rigid body at y=0.
         */
        CollisionShape bcs = new BoxCollisionShape(0.1f, 0.2f, 0.3f);
        PhysicsRigidBody prb = new PhysicsRigidBody(bcs, 1f);
        space.addCollisionObject(prb);
        /*
         * 50 iterations with a 20-msec timestep
         */
        for (int i = 0; i < 50; ++i) {
            space.update(0.02f, 0);
            //System.out.printf("location = %s%n", prb.getPhysicsLocation());
        }
        /*
         * Check the final location of the box.
         */
        Vector3f location = prb.getPhysicsLocation();
        Assert.assertEquals(0f, location.x, 0.2f);
        Assert.assertEquals(-0.8f, location.y, 0.04f);
        Assert.assertEquals(0f, location.z, 0.2f);
    }

    /**
     * Generate a collision shape using V-HACD.
     */
    @Test
    public void test002() {
        loadNativeLibrary();
        /*
         * Generate an L-shaped mesh: 12 vertices, 20 triangles
         */
        float[] positionArray = new float[]{
            0f, 0f, 0f,
            2f, 0f, 0f,
            2f, 1f, 0f,
            1f, 1f, 0f,
            1f, 3f, 0f,
            0f, 3f, 1f,
            0f, 0f, 1f,
            2f, 0f, 1f,
            2f, 1f, 1f,
            1f, 1f, 1f,
            1f, 3f, 1f,
            0f, 3f, 1f
        };
        int[] indexArray = new int[]{
            0, 1, 7, 0, 7, 6,
            0, 6, 11, 0, 11, 5,
            4, 5, 11, 4, 11, 10,
            3, 4, 10, 3, 10, 9,
            2, 3, 9, 2, 9, 8,
            1, 2, 8, 1, 8, 7,
            0, 3, 2, 0, 2, 1,
            0, 5, 4, 0, 4, 3,
            6, 8, 9, 6, 7, 8,
            6, 10, 11, 6, 9, 10
        };
        /*
         * Generate a hulls for the mesh.
         */
        VHACDParameters parameters = new VHACDParameters();
        List<VHACDHull> vhacdHulls
                = VHACD.compute(positionArray, indexArray, parameters);
        Assert.assertEquals(2, vhacdHulls.size());

        CompoundCollisionShape compound = new CompoundCollisionShape();
        int numHullVertices = 0;
        for (VHACDHull vhacdHull : vhacdHulls) {
            HullCollisionShape hullShape = new HullCollisionShape(vhacdHull);
            numHullVertices += hullShape.countHullVertices();
            compound.addChildShape(hullShape);
        }
        Assert.assertEquals(25, numHullVertices);
    }

    /**
     * Generate various collision shapes and verify their properties.
     */
    @Test
    public void test003() {
        loadNativeLibrary();
        FloatBuffer buf;
        /*
         * Box2d
         */
        Box2dShape box2d = new Box2dShape(1f, 2f);
        verifyCollisionShapeDefaults(box2d);
        Assert.assertEquals(0.04f, box2d.getMargin(), 0f);
        Assert.assertFalse(box2d.isConcave());
        Assert.assertTrue(box2d.isConvex());
        Assert.assertFalse(box2d.isInfinite());
        Assert.assertFalse(box2d.isNonMoving());
        Assert.assertFalse(box2d.isPolyhedral());
        buf = DebugShapeFactory.getDebugTriangles(box2d,
                DebugShapeFactory.lowResolution);
        Assert.assertEquals(108, buf.capacity());
        /*
         * Box
         */
        BoxCollisionShape box = new BoxCollisionShape(1f);
        verifyCollisionShapeDefaults(box);
        Assert.assertEquals(0.04f, box.getMargin(), 0f);
        Assert.assertFalse(box.isConcave());
        Assert.assertTrue(box.isConvex());
        Assert.assertFalse(box.isInfinite());
        Assert.assertFalse(box.isNonMoving());
        Assert.assertTrue(box.isPolyhedral());
        buf = DebugShapeFactory.getDebugTriangles(box,
                DebugShapeFactory.lowResolution);
        Assert.assertEquals(108, buf.capacity());
        /*
         * Capsule
         */
        CapsuleCollisionShape capsule = new CapsuleCollisionShape(1f, 1f);
        verifyCollisionShapeDefaults(capsule);
        Assert.assertEquals(PhysicsSpace.AXIS_Y, capsule.getAxis());
        Assert.assertEquals(1f, capsule.getHeight(), 0f);
        Assert.assertEquals(0f, capsule.getMargin(), 0f);
        Assert.assertFalse(capsule.isConcave());
        Assert.assertTrue(capsule.isConvex());
        Assert.assertFalse(capsule.isInfinite());
        Assert.assertFalse(capsule.isNonMoving());
        Assert.assertFalse(capsule.isPolyhedral());
        buf = DebugShapeFactory.getDebugTriangles(capsule,
                DebugShapeFactory.lowResolution);
        Assert.assertEquals(720, buf.capacity());
        /*
         * Compound without added children
         */
        CompoundCollisionShape compound = new CompoundCollisionShape();
        verifyCollisionShapeDefaults(compound);
        Assert.assertEquals(0, compound.countChildren());
        Assert.assertEquals(0.04f, compound.getMargin(), 0f);
        Assert.assertFalse(compound.isConcave());
        Assert.assertFalse(compound.isConvex());
        Assert.assertFalse(compound.isInfinite());
        Assert.assertFalse(compound.isNonMoving());
        Assert.assertFalse(compound.isPolyhedral());
        buf = DebugShapeFactory.getDebugTriangles(compound,
                DebugShapeFactory.lowResolution);
        Assert.assertEquals(0, buf.capacity());
        /*
         * Cone
         */
        ConeCollisionShape cone = new ConeCollisionShape(1f, 1f);
        verifyCollisionShapeDefaults(cone);
        Assert.assertEquals(PhysicsSpace.AXIS_Y, cone.getAxis());
        Assert.assertEquals(1f, cone.getHeight(), 0f);
        Assert.assertEquals(0.04f, cone.getMargin(), 0f);
        Assert.assertFalse(cone.isConcave());
        Assert.assertTrue(cone.isConvex());
        Assert.assertFalse(cone.isInfinite());
        Assert.assertFalse(cone.isNonMoving());
        Assert.assertFalse(cone.isPolyhedral());
        buf = DebugShapeFactory.getDebugTriangles(cone,
                DebugShapeFactory.lowResolution);
        Assert.assertEquals(702, buf.capacity());
        /*
         * Convex2d
         */
        ConeCollisionShape flatCone
                = new ConeCollisionShape(10f, 0f, PhysicsSpace.AXIS_Z);
        Convex2dShape convex2d = new Convex2dShape(flatCone);
        verifyCollisionShapeDefaults(convex2d);
        Assert.assertEquals(0.04f, convex2d.getMargin(), 0f);
        Assert.assertFalse(convex2d.isConcave());
        Assert.assertTrue(convex2d.isConvex());
        Assert.assertFalse(convex2d.isInfinite());
        Assert.assertFalse(convex2d.isNonMoving());
        Assert.assertFalse(convex2d.isPolyhedral());
        buf = DebugShapeFactory.getDebugTriangles(convex2d,
                DebugShapeFactory.lowResolution);
        Assert.assertEquals(504, buf.capacity());
        /*
         * Cylinder
         */
        CylinderCollisionShape cylinder
                = new CylinderCollisionShape(new Vector3f(1f, 1f, 1f));
        verifyCollisionShapeDefaults(cylinder);
        Assert.assertEquals(PhysicsSpace.AXIS_Z, cylinder.getAxis());
        Assert.assertEquals(2f, cylinder.getHeight(), 0f);
        Assert.assertEquals(0.04f, cylinder.getMargin(), 0f);
        Assert.assertFalse(cylinder.isConcave());
        Assert.assertTrue(cylinder.isConvex());
        Assert.assertFalse(cylinder.isInfinite());
        Assert.assertFalse(cylinder.isNonMoving());
        Assert.assertFalse(cylinder.isPolyhedral());
        buf = DebugShapeFactory.getDebugTriangles(cylinder,
                DebugShapeFactory.lowResolution);
        Assert.assertEquals(720, buf.capacity());
        /*
         * Empty
         */
        EmptyShape empty = new EmptyShape(true);
        verifyCollisionShapeDefaults(empty);
        Assert.assertEquals(0.04f, empty.getMargin(), 0f);
        Assert.assertTrue(empty.isConcave());
        Assert.assertFalse(empty.isConvex());
        Assert.assertFalse(empty.isInfinite());
        Assert.assertTrue(empty.isNonMoving());
        Assert.assertFalse(empty.isPolyhedral());
        Assert.assertEquals(0f, empty.unscaledVolume(), 0f);
        buf = DebugShapeFactory.getDebugTriangles(empty,
                DebugShapeFactory.lowResolution);
        Assert.assertEquals(0, buf.capacity());
        /*
         * Heightfield
         */
        float[] nineHeights = {1f, 0f, 1f, 0f, 0.5f, 0f, 1f, 0f, 1f};
        HeightfieldCollisionShape hcs
                = new HeightfieldCollisionShape(nineHeights);
        verifyCollisionShapeDefaults(hcs);
        Assert.assertEquals(9, hcs.countMeshVertices());
        Assert.assertEquals(0.04f, hcs.getMargin(), 0f);
        Assert.assertTrue(hcs.isConcave());
        Assert.assertFalse(hcs.isConvex());
        Assert.assertFalse(hcs.isInfinite());
        Assert.assertTrue(hcs.isNonMoving());
        Assert.assertFalse(hcs.isPolyhedral());
        buf = DebugShapeFactory.getDebugTriangles(hcs,
                DebugShapeFactory.lowResolution);
        Assert.assertEquals(72, buf.capacity());
        /*
         * Hull
         */
        List<Vector3f> prismVertices = new ArrayList<>(6);
        prismVertices.add(new Vector3f(1f, 1f, 1f));
        prismVertices.add(new Vector3f(1f, 1f, -1f));
        prismVertices.add(new Vector3f(-1f, 1f, 0f));
        prismVertices.add(new Vector3f(1f, -1f, 1f));
        prismVertices.add(new Vector3f(1f, -1f, -1f));
        prismVertices.add(new Vector3f(-1f, -1f, 0f));
        HullCollisionShape hull = new HullCollisionShape(prismVertices);
        verifyCollisionShapeDefaults(hull);
        Assert.assertEquals(8f, hull.aabbVolume(), 0.001f);
        Assert.assertEquals(6, hull.countHullVertices());
        Assert.assertEquals(6, hull.countMeshVertices());
        Assert.assertEquals(0.04f, hull.getMargin(), 0f);
        Assert.assertFalse(hull.isConcave());
        Assert.assertTrue(hull.isConvex());
        Assert.assertFalse(hull.isInfinite());
        Assert.assertFalse(hull.isNonMoving());
        Assert.assertTrue(hull.isPolyhedral());
        buf = DebugShapeFactory.getDebugTriangles(hull,
                DebugShapeFactory.lowResolution);
        Assert.assertEquals(720, buf.capacity());
        /*
         * MultiSphere
         */
        MultiSphere multiSphere = new MultiSphere(1f);
        verifyCollisionShapeDefaults(multiSphere);
        assertEquals(0f, 0f, 0f, multiSphere.copyCenter(0, null), 0f);
        Assert.assertEquals(1, multiSphere.countSpheres());
        Assert.assertEquals(1f, multiSphere.getRadius(0), 0f);
        Assert.assertFalse(multiSphere.isConcave());
        Assert.assertTrue(multiSphere.isConvex());
        Assert.assertFalse(multiSphere.isInfinite());
        Assert.assertFalse(multiSphere.isNonMoving());
        Assert.assertFalse(multiSphere.isPolyhedral());
        buf = DebugShapeFactory.getDebugTriangles(multiSphere,
                DebugShapeFactory.lowResolution);
        Assert.assertEquals(720, buf.capacity());
        /*
         * Plane
         */
        Plane plane = new Plane(new Vector3f(0f, 1f, 0f), 0f);
        PlaneCollisionShape pcs = new PlaneCollisionShape(plane);
        verifyCollisionShapeDefaults(pcs);
        Assert.assertEquals(0.04f, pcs.getMargin(), 0f);
        Assert.assertEquals(0f, pcs.getPlane().getConstant(), 0f);
        assertEquals(0f, 1f, 0f, pcs.getPlane().getNormal(), 0f);
        Assert.assertTrue(pcs.isConcave());
        Assert.assertFalse(pcs.isConvex());
        Assert.assertTrue(pcs.isInfinite());
        Assert.assertTrue(pcs.isNonMoving());
        Assert.assertFalse(pcs.isPolyhedral());
        buf = DebugShapeFactory.getDebugTriangles(pcs,
                DebugShapeFactory.lowResolution);
        Assert.assertEquals(18, buf.capacity());
        /*
         * Simplex of 1 vertex
         */
        SimplexCollisionShape simplex1
                = new SimplexCollisionShape(new Vector3f(0f, 0f, 0f));
        verifySimplexDefaults(simplex1);
        Assert.assertEquals(1, simplex1.countMeshVertices());
        assertEquals(0f, 0f, 0f, simplex1.copyVertex(0, null), 0f);
        assertEquals(0f, 0f, 0f, simplex1.getHalfExtents(null), 0f);
        buf = DebugShapeFactory.getDebugTriangles(simplex1,
                DebugShapeFactory.lowResolution);
        Assert.assertEquals(720, buf.capacity());
        /*
         * Simplex of 2 vertices
         */
        SimplexCollisionShape simplex2 = new SimplexCollisionShape(
                new Vector3f(1f, 0f, 0f), new Vector3f(-1, 0f, 0f));
        verifySimplexDefaults(simplex2);
        Assert.assertEquals(2, simplex2.countMeshVertices());
        assertEquals(1f, 0f, 0f, simplex2.copyVertex(0, null), 0f);
        assertEquals(-1f, 0f, 0f, simplex2.copyVertex(1, null), 0f);
        assertEquals(1f, 0f, 0f, simplex2.getHalfExtents(null), 0f);
        buf = DebugShapeFactory.getDebugTriangles(simplex2,
                DebugShapeFactory.lowResolution);
        Assert.assertEquals(720, buf.capacity());
        /*
         * Simplex of 3 vertices
         */
        SimplexCollisionShape simplex3 = new SimplexCollisionShape(
                new Vector3f(0f, 1f, 1f),
                new Vector3f(1f, 1f, 0f),
                new Vector3f(1f, 0f, 1f)
        );
        verifySimplexDefaults(simplex3);
        Assert.assertEquals(3, simplex3.countMeshVertices());
        assertEquals(0f, 1f, 1f, simplex3.copyVertex(0, null), 0f);
        assertEquals(1f, 1f, 0f, simplex3.copyVertex(1, null), 0f);
        assertEquals(1f, 0f, 1f, simplex3.copyVertex(2, null), 0f);
        assertEquals(1f, 1f, 1f, simplex3.getHalfExtents(null), 0f);
        buf = DebugShapeFactory.getDebugTriangles(simplex3,
                DebugShapeFactory.lowResolution);
        Assert.assertEquals(720, buf.capacity());
        /*
         * Simplex of 4 vertices
         */
        SimplexCollisionShape simplex4 = new SimplexCollisionShape(
                new Vector3f(0f, 1f, 1f),
                new Vector3f(0f, 1f, -1f),
                new Vector3f(1f, -1f, 0f),
                new Vector3f(-1f, -1f, 0f)
        );
        verifySimplexDefaults(simplex4);
        Assert.assertEquals(4, simplex4.countMeshVertices());
        assertEquals(0f, 1f, 1f, simplex4.copyVertex(0, null), 0f);
        assertEquals(0f, 1f, -1f, simplex4.copyVertex(1, null), 0f);
        assertEquals(1f, -1f, 0f, simplex4.copyVertex(2, null), 0f);
        assertEquals(-1f, -1f, 0f, simplex4.copyVertex(3, null), 0f);
        assertEquals(1f, 1f, 1f, simplex4.getHalfExtents(null), 0f);
        buf = DebugShapeFactory.getDebugTriangles(simplex4,
                DebugShapeFactory.lowResolution);
        Assert.assertEquals(720, buf.capacity());
        /*
         * Sphere
         */
        SphereCollisionShape sphere = new SphereCollisionShape(1f);
        verifyCollisionShapeDefaults(sphere);
        Assert.assertEquals(0f, sphere.getMargin(), 0f);
        Assert.assertFalse(sphere.isConcave());
        Assert.assertTrue(sphere.isConvex());
        Assert.assertFalse(sphere.isInfinite());
        Assert.assertFalse(sphere.isNonMoving());
        Assert.assertFalse(sphere.isPolyhedral());
        buf = DebugShapeFactory.getDebugTriangles(sphere,
                DebugShapeFactory.lowResolution);
        Assert.assertEquals(720, buf.capacity());
    }

    /**
     * Perform ray tests against a unit sphere in various collision spaces.
     */
    @Test
    public void test004() {
        loadNativeLibrary();

        float radius = 1f;
        CollisionShape sphereShape = new SphereCollisionShape(radius);

        CollisionSpace space = new CollisionSpace(Vector3f.ZERO,
                Vector3f.ZERO, PhysicsSpace.BroadphaseType.SIMPLE);
        verifyCollisionSpaceDefaults(space);
        performRayTests(sphereShape, space);

        space = new CollisionSpace(new Vector3f(-10f, -10f, -10f),
                new Vector3f(10f, 10f, 10f),
                PhysicsSpace.BroadphaseType.AXIS_SWEEP_3);
        verifyCollisionSpaceDefaults(space);
        performRayTests(sphereShape, space);

        space = new CollisionSpace(new Vector3f(-10f, -10f, -10f),
                new Vector3f(10f, 10f, 10f),
                PhysicsSpace.BroadphaseType.AXIS_SWEEP_3_32);
        verifyCollisionSpaceDefaults(space);
        performRayTests(sphereShape, space);

        space = new CollisionSpace(Vector3f.ZERO, Vector3f.ZERO,
                PhysicsSpace.BroadphaseType.DBVT);
        verifyCollisionSpaceDefaults(space);
        performRayTests(sphereShape, space);
        /*
         * Physics spaces with various broadphase accelerators.
         */
        space = new PhysicsSpace(PhysicsSpace.BroadphaseType.SIMPLE);
        verifyCollisionSpaceDefaults(space);
        performRayTests(sphereShape, space);

        space = new PhysicsSpace(new Vector3f(-10f, -10f, -10f),
                new Vector3f(10f, 10f, 10f),
                PhysicsSpace.BroadphaseType.AXIS_SWEEP_3);
        verifyCollisionSpaceDefaults(space);
        performRayTests(sphereShape, space);

        space = new PhysicsSpace(new Vector3f(-10f, -10f, -10f),
                new Vector3f(10f, 10f, 10f),
                PhysicsSpace.BroadphaseType.AXIS_SWEEP_3_32);
        verifyCollisionSpaceDefaults(space);
        performRayTests(sphereShape, space);

        space = new PhysicsSpace(PhysicsSpace.BroadphaseType.DBVT);
        verifyCollisionSpaceDefaults(space);
        performRayTests(sphereShape, space);
        /*
         * Soft spaces with various broadphase accelerators.
         */
        space = new PhysicsSoftSpace(Vector3f.ZERO,
                Vector3f.ZERO, PhysicsSpace.BroadphaseType.SIMPLE);
        verifyCollisionSpaceDefaults(space);
        performRayTests(sphereShape, space);

        space = new PhysicsSoftSpace(new Vector3f(-10f, -10f, -10f),
                new Vector3f(10f, 10f, 10f),
                PhysicsSpace.BroadphaseType.AXIS_SWEEP_3);
        verifyCollisionSpaceDefaults(space);
        performRayTests(sphereShape, space);

        space = new PhysicsSoftSpace(new Vector3f(-10f, -10f, -10f),
                new Vector3f(10f, 10f, 10f),
                PhysicsSpace.BroadphaseType.AXIS_SWEEP_3_32);
        verifyCollisionSpaceDefaults(space);
        performRayTests(sphereShape, space);

        space = new PhysicsSoftSpace(Vector3f.ZERO, Vector3f.ZERO,
                PhysicsSpace.BroadphaseType.DBVT);
        verifyCollisionSpaceDefaults(space);
        performRayTests(sphereShape, space);
        /*
         * Multibody spaces with various broadphase accelerators.
         */
        space = new MultiBodySpace(Vector3f.ZERO,
                Vector3f.ZERO, PhysicsSpace.BroadphaseType.SIMPLE);
        verifyCollisionSpaceDefaults(space);
        performRayTests(sphereShape, space);

        space = new MultiBodySpace(new Vector3f(-10f, -10f, -10f),
                new Vector3f(10f, 10f, 10f),
                PhysicsSpace.BroadphaseType.AXIS_SWEEP_3);
        verifyCollisionSpaceDefaults(space);
        performRayTests(sphereShape, space);

        space = new MultiBodySpace(new Vector3f(-10f, -10f, -10f),
                new Vector3f(10f, 10f, 10f),
                PhysicsSpace.BroadphaseType.AXIS_SWEEP_3_32);
        verifyCollisionSpaceDefaults(space);
        performRayTests(sphereShape, space);

        space = new MultiBodySpace(Vector3f.ZERO, Vector3f.ZERO,
                PhysicsSpace.BroadphaseType.DBVT);
        verifyCollisionSpaceDefaults(space);
        performRayTests(sphereShape, space);
    }

    /**
     * Perform drop tests using a box-shaped rigid body.
     */
    @Test
    public void test005() {
        loadNativeLibrary();

        float radius = 0.2f;
        CollisionShape boxShape = new BoxCollisionShape(radius);
        /*
         * Physics spaces with various broadphase accelerators.
         */
        PhysicsSpace space
                = new PhysicsSpace(PhysicsSpace.BroadphaseType.SIMPLE);
        verifyPhysicsSpaceDefaults(space);
        performDropTest(boxShape, space);

        space = new PhysicsSpace(new Vector3f(-10f, -10f, -10f),
                new Vector3f(10f, 10f, 10f),
                PhysicsSpace.BroadphaseType.AXIS_SWEEP_3);
        verifyPhysicsSpaceDefaults(space);
        performDropTest(boxShape, space);

        space = new PhysicsSpace(new Vector3f(-10f, -10f, -10f),
                new Vector3f(10f, 10f, 10f),
                PhysicsSpace.BroadphaseType.AXIS_SWEEP_3_32);
        verifyPhysicsSpaceDefaults(space);
        performDropTest(boxShape, space);

        space = new PhysicsSpace(PhysicsSpace.BroadphaseType.DBVT);
        verifyPhysicsSpaceDefaults(space);
        performDropTest(boxShape, space);
        /*
         * Soft spaces with various broadphase accelerators.
         */
        space = new PhysicsSoftSpace(Vector3f.ZERO, Vector3f.ZERO,
                PhysicsSpace.BroadphaseType.SIMPLE);
        verifyPhysicsSpaceDefaults(space);
        performDropTest(boxShape, space);

        space = new PhysicsSoftSpace(new Vector3f(-10f, -10f, -10f),
                new Vector3f(10f, 10f, 10f),
                PhysicsSpace.BroadphaseType.AXIS_SWEEP_3);
        verifyPhysicsSpaceDefaults(space);
        performDropTest(boxShape, space);

        space = new PhysicsSoftSpace(new Vector3f(-10f, -10f, -10f),
                new Vector3f(10f, 10f, 10f),
                PhysicsSpace.BroadphaseType.AXIS_SWEEP_3_32);
        verifyPhysicsSpaceDefaults(space);
        performDropTest(boxShape, space);

        space = new PhysicsSoftSpace(Vector3f.ZERO,
                Vector3f.ZERO, PhysicsSpace.BroadphaseType.DBVT);
        verifyPhysicsSpaceDefaults(space);
        performDropTest(boxShape, space);
        /*
         * Multibody spaces with various broadphase accelerators.
         */
        space = new MultiBodySpace(Vector3f.ZERO,
                Vector3f.ZERO, PhysicsSpace.BroadphaseType.SIMPLE);
        verifyCollisionSpaceDefaults(space);
        performDropTest(boxShape, space);

        space = new MultiBodySpace(new Vector3f(-10f, -10f, -10f),
                new Vector3f(10f, 10f, 10f),
                PhysicsSpace.BroadphaseType.AXIS_SWEEP_3);
        verifyCollisionSpaceDefaults(space);
        performDropTest(boxShape, space);

        space = new MultiBodySpace(new Vector3f(-10f, -10f, -10f),
                new Vector3f(10f, 10f, 10f),
                PhysicsSpace.BroadphaseType.AXIS_SWEEP_3_32);
        verifyCollisionSpaceDefaults(space);
        performDropTest(boxShape, space);

        space = new MultiBodySpace(Vector3f.ZERO, Vector3f.ZERO,
                PhysicsSpace.BroadphaseType.DBVT);
        verifyCollisionSpaceDefaults(space);
        performDropTest(boxShape, space);
    }

    /**
     * Construct a MultiBody and add it to a MultiBodySpace.
     */
    @Test
    public void test006() {
        loadNativeLibrary();

        int numLinks = 5;
        float baseMass = 1f;
        Vector3f baseInertia = Vector3f.UNIT_XYZ;
        boolean fixedBase = true;
        boolean canSleep = true;
        MultiBody multiBody = new MultiBody(numLinks, baseMass, baseInertia,
                fixedBase, canSleep);

        Assert.assertEquals(0.04f, multiBody.angularDamping(), 0f);
        assertEquals(0f, 0f, 0f, multiBody.angularMomentum(null), 0f);
        assertEquals(0f, 0f, 0f, multiBody.baseForce(null), 0f);
        assertEquals(0f, 0f, 0f, multiBody.baseLocation(null), 0f);
        assertEquals(0f, 0f, 0f, 1f, multiBody.baseOrientation(null), 0f);
        assertEquals(0f, 0f, 0f, multiBody.baseTorque(null), 0f);
        assertEquals(0f, 0f, 0f, multiBody.baseVelocity(null), 0f);
        Assert.assertTrue(multiBody.canWakeup());
        Assert.assertEquals(PhysicsCollisionObject.COLLISION_GROUP_01,
                multiBody.collideWithGroups());
        Assert.assertEquals(PhysicsCollisionObject.COLLISION_GROUP_01,
                multiBody.collisionGroup());
        Assert.assertEquals(0, multiBody.countDofs());
        Assert.assertEquals(0, multiBody.countConfiguredLinks());
        Assert.assertEquals(0, multiBody.countPositionVariables());
        Assert.assertNull(multiBody.getBaseCollider());
        Assert.assertNotEquals(0L, multiBody.nativeId());
        Assert.assertFalse(multiBody.isUsingGlobalVelocities());
        Assert.assertTrue(multiBody.isUsingGyroTerm());
        Assert.assertFalse(multiBody.isUsingRK4());
        Assert.assertEquals(0f, multiBody.kineticEnergy(), 0f);
        Assert.assertEquals(0.04f, multiBody.linearDamping(), 0f);
        Assert.assertEquals(1000f, multiBody.maxAppliedImpulse(), 0f);
        Assert.assertEquals(100f, multiBody.maxCoordinateVelocity(), 0f);
        Assert.assertEquals(0L, multiBody.spaceId());

        float radius = 0.4f;
        CollisionShape shape = new SphereCollisionShape(radius);

        multiBody.addBaseCollider(shape);
        Assert.assertNotNull(multiBody.getBaseCollider());

        float linkMass = 0.1f;
        Vector3f linkInertia = new Vector3f(0.1f, 0.1f, 0.1f);

        MultiBodyLink link0 = multiBody.configureFixedLink(linkMass,
                linkInertia, null, Quaternion.IDENTITY, Vector3f.UNIT_X,
                Vector3f.UNIT_X);
        Assert.assertNull(link0.getCollider());
        link0.addCollider(shape);

        Assert.assertEquals(link0, multiBody.getLink(0));
        assertEquals(0f, 0f, 0f, link0.appliedForce(null), 0f);
        assertEquals(0f, 0f, 0f, link0.appliedTorque(null), 0f);
        assertEquals(0f, 0f, 0f, link0.constraintForce(null), 0f);
        assertEquals(0f, 0f, 0f, link0.constraintTorque(null), 0f);
        Assert.assertEquals(0, link0.countDofs());
        Assert.assertEquals(0, link0.countPositionVariables());
        Assert.assertNotNull(link0.getCollider());
        Assert.assertEquals(multiBody, link0.getMultiBody());
        Assert.assertNull(link0.getParentLink());
        Assert.assertEquals(0, link0.index());
        assertEquals(0.1f, 0.1f, 0.1f, link0.inertia(null), 0f);
        Assert.assertFalse(link0.isCollisionWithParent());
        Assert.assertEquals(MultiBodyJointType.Fixed, link0.jointType());
        Assert.assertEquals(linkMass, link0.mass(), 0f);
        assertEquals(0f, 0f, 0f, 1f, link0.orientation(null), 0f);
        assertEquals(1f, 0f, 0f, link0.parent2Pivot(null), 0f);
        assertEquals(1f, 0f, 0f, link0.pivot2Link(null), 0f);

        boolean disableCollision = true;
        MultiBodyLink link1 = multiBody.configurePlanarLink(linkMass,
                linkInertia, link0, Quaternion.IDENTITY, Vector3f.UNIT_Y,
                Vector3f.UNIT_X, disableCollision);

        Assert.assertEquals(link1, multiBody.getLink(1));
        assertEquals(0f, 1f, 0f, link1.axis(null), 1e-6f);
        Assert.assertEquals(3, link1.countDofs());
        Assert.assertEquals(3, link1.countPositionVariables());
        Assert.assertEquals(link0, link1.getParentLink());
        Assert.assertEquals(1, link1.index());
        Assert.assertFalse(link1.isCollisionWithParent());
        Assert.assertEquals(0f, link1.jointPosition(2), 0f);
        Assert.assertEquals(0f, link1.jointTorque(2), 0f);
        Assert.assertEquals(MultiBodyJointType.Planar, link1.jointType());
        Assert.assertEquals(0f, link1.jointVelocity(2), 0f);
        assertEquals(0f, 0f, 0f, 1f, link1.orientation(null), 0f);
        assertEquals(1f, 0f, 0f, link1.parent2Link(null), 0f);

        MultiBodyLink link2 = multiBody.configurePrismaticLink(linkMass,
                linkInertia, link1, Quaternion.IDENTITY, Vector3f.UNIT_Y,
                Vector3f.UNIT_X, Vector3f.UNIT_X, disableCollision);
        link2.addCollider(shape);

        Assert.assertEquals(link2, multiBody.getLink(2));
        assertEquals(0f, 0f, 0f, link2.appliedForce(null), 0f);
        assertEquals(0f, 1f, 0f, link2.axis(null), 1e-6f);
        Assert.assertEquals(1, link2.countDofs());
        Assert.assertEquals(1, link2.countPositionVariables());
        Assert.assertFalse(link2.isCollisionWithParent());
        Assert.assertEquals(MultiBodyJointType.Prismatic, link2.jointType());
        assertEquals(0f, 0f, 0f, 1f, link2.orientation(null), 0f);
        assertEquals(1f, 0f, 0f, link2.parent2Pivot(null), 0f);
        assertEquals(1f, 0f, 0f, link2.pivot2Link(null), 0f);

        MultiBodyLink link3 = multiBody.configureRevoluteLink(linkMass,
                linkInertia, link2, Quaternion.IDENTITY, Vector3f.UNIT_Y,
                Vector3f.UNIT_X, Vector3f.UNIT_X, disableCollision);

        Assert.assertEquals(link3, multiBody.getLink(3));
        assertEquals(0f, 0f, 0f, link3.appliedForce(null), 0f);
        assertEquals(0f, 1f, 0f, link3.axis(null), 1e-6f);
        Assert.assertEquals(1, link3.countDofs());
        Assert.assertEquals(1, link3.countPositionVariables());
        Assert.assertNull(link3.getCollider());
        Assert.assertFalse(link3.isCollisionWithParent());
        Assert.assertEquals(MultiBodyJointType.Revolute, link3.jointType());
        assertEquals(0f, 0f, 0f, 1f, link3.orientation(null), 0f);
        assertEquals(1f, 0f, 0f, link3.parent2Pivot(null), 0f);
        assertEquals(1f, 0f, 0f, link3.pivot2Link(null), 0f);

        boolean enableCollision = false;
        MultiBodyLink link4 = multiBody.configureSphericalLink(linkMass,
                linkInertia, link3, Quaternion.IDENTITY, Vector3f.UNIT_X,
                Vector3f.UNIT_X, enableCollision);
        link4.addCollider(shape);

        Assert.assertEquals(link4, multiBody.getLink(4));
        assertEquals(0f, 0f, 0f, link4.appliedForce(null), 0f);
        Assert.assertEquals(3, link4.countDofs());
        Assert.assertEquals(4, link4.countPositionVariables());
        Assert.assertTrue(link4.isCollisionWithParent());
        Assert.assertEquals(MultiBodyJointType.Spherical, link4.jointType());
        assertEquals(0f, 0f, 0f, 1f, link4.orientation(null), 0f);
        assertEquals(1f, 0f, 0f, link4.parent2Pivot(null), 0f);
        assertEquals(1f, 0f, 0f, link4.pivot2Link(null), 0f);

        Assert.assertEquals(5, multiBody.countConfiguredLinks());
        Assert.assertEquals(8, multiBody.countDofs());
        Assert.assertEquals(9, multiBody.countPositionVariables());

        MultiBodySpace space = new MultiBodySpace(Vector3f.ZERO, Vector3f.ZERO,
                PhysicsSpace.BroadphaseType.DBVT);
        verifyCollisionSpaceDefaults(space);

        space.add(multiBody);

        Assert.assertEquals(space.getSpaceId(), multiBody.spaceId());
        Assert.assertEquals(4, space.countCollisionObjects());
        Assert.assertEquals(0, space.countJoints());
        Assert.assertEquals(1, space.countMultiBodies());
        Assert.assertEquals(0, space.countRigidBodies());
        Assert.assertFalse(space.isEmpty());

        space.remove(multiBody);

        Assert.assertEquals(0L, multiBody.spaceId());
        Assert.assertEquals(0, space.countCollisionObjects());
        Assert.assertEquals(0, space.countMultiBodies());
        Assert.assertTrue(space.isEmpty());
    }
    // *************************************************************************
    // private methods

    private static void assertEquals(float x, float y, float z, float w,
            Quaternion q, float tolerance) {
        Assert.assertEquals(x, q.getX(), tolerance);
        Assert.assertEquals(y, q.getY(), tolerance);
        Assert.assertEquals(z, q.getZ(), tolerance);
        Assert.assertEquals(w, q.getW(), tolerance);
    }

    private static void assertEquals(float x, float y, float z, Vector3f vector,
            float tolerance) {
        Assert.assertEquals(x, vector.x, tolerance);
        Assert.assertEquals(y, vector.y, tolerance);
        Assert.assertEquals(z, vector.z, tolerance);
    }

    private static void loadNativeLibrary() {
        boolean loadFromDist = false;

        File directory;
        if (loadFromDist) {
            directory = new File("dist");
        } else {
            directory = new File("build/libs/bulletjme/shared");
        }
        NativeLibraryLoader.loadLibbulletjme(loadFromDist, directory,
                "Debug", "Sp");
        Assert.assertTrue(NativeLibrary.isDebug());
        Assert.assertFalse(NativeLibrary.isDoublePrecision());
    }

    /**
     * Perform a drop test using the specified shape in the specified space.
     *
     * @param dropShape the shape to drop (not null)
     * @param space the space in which to perform the test (not null, modified)
     */
    private static void performDropTest(CollisionShape dropShape,
            PhysicsSpace space) {
        Assert.assertTrue(space.isEmpty());
        /*
         * Add a static horizontal plane at y=-1.
         */
        Plane plane = new Plane(Vector3f.UNIT_Y, -1f);
        CollisionShape floorShape = new PlaneCollisionShape(plane);
        float mass = PhysicsBody.massForStatic;
        PhysicsRigidBody floorBody = new PhysicsRigidBody(floorShape, mass);
        space.addCollisionObject(floorBody);

        Assert.assertFalse(space.isEmpty());
        Assert.assertEquals(1, space.countCollisionObjects());
        Assert.assertEquals(1, space.countRigidBodies());
        Assert.assertTrue(space.contains(floorBody));
        /*
         * Add a dynamic rigid body at y=0.
         */
        mass = 1f;
        PhysicsRigidBody dropBody = new PhysicsRigidBody(dropShape, mass);
        space.addCollisionObject(dropBody);

        Assert.assertFalse(space.isEmpty());
        Assert.assertEquals(2, space.countCollisionObjects());
        Assert.assertEquals(2, space.countRigidBodies());
        Assert.assertTrue(space.contains(floorBody));
        Assert.assertTrue(space.contains(dropBody));
        /*
         * 50 iterations with a 20-msec timestep
         */
        for (int i = 0; i < 50; ++i) {
            space.update(0.02f, 0);
            //System.out.printf("location = %s%n", prb.getPhysicsLocation());
        }
        /*
         * Check the final location of the body.
         */
        Vector3f location = dropBody.getPhysicsLocation();
        Assert.assertEquals(0f, location.x, 0.2f);
        Assert.assertEquals(0f, location.z, 0.2f);

        space.remove(floorBody);
        space.remove(dropBody);

        Assert.assertTrue(space.isEmpty());
    }

    /**
     * Perform ray tests against the specified shape in the specified space.
     *
     * @param shape the shape to add to the space (not null)
     * @param space the space in which to perform the tests (not null, modified)
     */
    private static void performRayTests(CollisionShape shape,
            CollisionSpace space) {
        Assert.assertTrue(space.isEmpty());

        PhysicsGhostObject ghost = new PhysicsGhostObject(shape);
        space.add(ghost);
        Assert.assertTrue(space.contains(ghost));
        Assert.assertEquals(1, space.countCollisionObjects());

        List<PhysicsRayTestResult> results0 = space.rayTest(
                new Vector3f(0.8f, 0.8f, 2f), new Vector3f(0.8f, 0.8f, 0f));
        Assert.assertEquals(0, results0.size());

        List<PhysicsRayTestResult> results1 = space.rayTest(
                new Vector3f(0.7f, 0.7f, 2f), new Vector3f(0.7f, 0.7f, 0f));
        Assert.assertEquals(1, results1.size());

        List<PhysicsRayTestResult> results2 = space.rayTest(
                new Vector3f(0.7f, 0.7f, 2f), new Vector3f(0.7f, 0.7f, -2f));
        Assert.assertEquals(1, results2.size());

        space.remove(ghost);
        Assert.assertTrue(space.isEmpty());

        List<PhysicsRayTestResult> results3 = space.rayTest(
                new Vector3f(0.7f, 0.7f, 2f), new Vector3f(0.7f, 0.7f, -2f));
        Assert.assertEquals(0, results3.size());
    }

    /**
     * Verify defaults common to all newly-created collision shapes.
     *
     * @param shape the shape to test (not null, unaffected)
     */
    private static void verifyCollisionShapeDefaults(CollisionShape shape) {
        Assert.assertNotNull(shape);
        Assert.assertNotEquals(0L, shape.getObjectId());
        assertEquals(1f, 1f, 1f, shape.getScale(null), 0f);
    }

    /**
     * Verify defaults common to all newly-created collision spaces.
     *
     * @param space the space to test (not null, unaffected)
     */
    private static void verifyCollisionSpaceDefaults(CollisionSpace space) {
        Assert.assertNotNull(space);
        Assert.assertTrue(space.isEmpty());
        Assert.assertEquals(0, space.countCollisionObjects());
        Assert.assertEquals(RayTestFlag.SubSimplexRaytest,
                space.getRayTestFlags());
    }

    /**
     * Verify defaults common to all newly-created physics spaces.
     *
     * @param space the space to test (not null, unaffected)
     */
    private static void verifyPhysicsSpaceDefaults(PhysicsSpace space) {
        verifyCollisionSpaceDefaults(space);
        Assert.assertEquals(0, space.countJoints());
        Assert.assertEquals(0, space.countRigidBodies());
        Assert.assertEquals(1 / 60f, space.getAccuracy(), 0f);
        Assert.assertEquals(0f, space.getGlobalCfm(), 0f);
        assertEquals(0f, -9.81f, 0f, space.getGravity(null), 0f);
        Assert.assertEquals(4, space.maxSubSteps());
        Assert.assertEquals(0.1f, space.maxTimeStep(), 0f);
        Assert.assertEquals(10, space.getSolverNumIterations());

        if (space instanceof MultiBodySpace) {
            MultiBodySpace mbSpace = (MultiBodySpace) space;
            Assert.assertEquals(0, mbSpace.countMultiBodies());

        } else if (space instanceof PhysicsSoftSpace) {
            PhysicsSoftSpace softSpace = (PhysicsSoftSpace) space;
            Assert.assertEquals(0, softSpace.countSoftBodies());
        }
    }

    /**
     * Verify defaults common to all newly-created simplex shapes.
     *
     * @param simplex the shape to test (not null, unaffected)
     */
    private static void verifySimplexDefaults(SimplexCollisionShape simplex) {
        verifyCollisionShapeDefaults(simplex);
        Assert.assertEquals(0.04f, simplex.getMargin(), 0f);

        Assert.assertFalse(simplex.isConcave());
        Assert.assertTrue(simplex.isConvex());
        Assert.assertFalse(simplex.isInfinite());
        Assert.assertFalse(simplex.isNonMoving());
        Assert.assertTrue(simplex.isPolyhedral());
    }
}
