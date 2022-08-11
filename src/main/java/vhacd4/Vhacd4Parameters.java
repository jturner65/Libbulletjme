/*
 Copyright (c) 2016, Riccardo Balbo
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:

 1. Redistributions of source code must retain the above copyright notice, this
    list of conditions and the following disclaimer.

 2. Redistributions in binary form must reproduce the above copyright notice,
    this list of conditions and the following disclaimer in the documentation
    and/or other materials provided with the distribution.

 3. Neither the name of the copyright holder nor the names of its
    contributors may be used to endorse or promote products derived from
    this software without specific prior written permission.

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
package vhacd4;

import com.jme3.bullet.FillMode;
import com.jme3.bullet.NativePhysicsObject;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;
import jme3utilities.Validate;

/**
 * A set of tuning parameters for convex decomposition, based on version 4 of
 * V-HACD's IVHACD::Parameters.
 */
public class Vhacd4Parameters
        extends NativePhysicsObject
        implements Cloneable {
    // *************************************************************************
    // constants and loggers

    /**
     * message logger for this class
     */
    final public static Logger logger
            = Logger.getLogger(Vhacd4Parameters.class.getName());
    // *************************************************************************
    // fields

    /**
     * true&rarr;enable debug output
     */
    private boolean debug;
    // *************************************************************************
    // constructors

    /**
     * Instantiate the default tuning parameters.
     */
    public Vhacd4Parameters() {
        long objectId = create();
        super.setNativeId(objectId);

        setMaxNumVerticesPerCH(objectId, 32);
        setResolution(objectId, 100_000);
    }
    // *************************************************************************
    // new methods exposed

    /**
     * Read all parameters from the specified InputStream.
     *
     * @param is the stream to read (not null)
     * @throws IOException from DataInputStream
     */
    public void fromInputStream(InputStream is) throws IOException {
        DataInputStream dis = new DataInputStream(is);

        setAsync(dis.readBoolean());
        setDebugEnabled(dis.readBoolean());

        int fillModeOrdinal = dis.readInt();
        FillMode fillMode = FillMode.values()[fillModeOrdinal];
        setFillMode(fillMode);

        setFindBestPlane(dis.readBoolean());
        setMaxHulls(dis.readInt());
        setMaxRecursion(dis.readInt());
        setMaxVerticesPerHull(dis.readInt());
        setMinEdgeLength(dis.readInt());
        setShrinkWrap(dis.readBoolean());
        setVolumePercentError(dis.readDouble());
        setVoxelResolution(dis.readInt());
    }

    /**
     * Test whether debug output is enabled.
     *
     * @return true if enabled, otherwise false
     */
    public boolean getDebugEnabled() {
        return debug;
    }

    /**
     * Return the algorithm to generate a solid object from voxels (native
     * field: m_fillMode).
     *
     * @return an enum value (not null)
     */
    public FillMode getFillMode() {
        long objectId = nativeId();
        int ordinal = getFillMode(objectId);
        FillMode result = FillMode.values()[ordinal];

        return result;
    }

    /**
     * Return the maximum number of convex hulls (native field:
     * m_maxConvexHulls).
     *
     * @return the limit (&ge;1, &le;1024)
     */
    public int getMaxHulls() {
        long objectId = nativeId();
        int result = getMaxHulls(objectId);

        return result;
    }

    /**
     * Return the maximum recursion depth (native field: m_maxRecursionDepth).
     *
     * @return the limit (&ge;1, &le;1024)
     */
    public int getMaxRecursion() {
        long objectId = nativeId();
        int result = getMaxRecursion(objectId);

        return result;
    }

    /**
     * Return the maximum number of vertices per convex hull (native field:
     * m_maxNumVerticesPerCH).
     *
     * @return the limit (&ge;4, &le;1024)
     */
    public int getMaxVerticesPerHull() {
        long objectId = nativeId();
        int result = getMaxNumVerticesPerCH(objectId);

        return result;
    }

    /**
     * Return the minimum edge length (native field: m_minEdgeLength).
     *
     * @return the limit (&ge;1, &le;1024)
     */
    public int getMinEdgeLength() {
        long objectId = nativeId();
        int result = getMinEdgeLength(objectId);

        return result;
    }

    /**
     * Return the tolerance for the percent difference between the voxel volume
     * and the volume of the hull (native field:
     * m_minimumVolumePercentErrorAllowed).
     *
     * @return the percentage (&gt;0, &lt;100)
     */
    public double getVolumePercentError() {
        long objectId = nativeId();
        double result = getVolumePercentError(objectId);

        return result;
    }

    /**
     * Return the maximum number of voxels generated during the voxelization
     * stage (native field: m_resolution).
     *
     * @return the limit (&ge;10000, &le;64000000)
     */
    public int getVoxelResolution() {
        long objectId = nativeId();
        int result = getResolution(objectId);

        return result;
    }

    /**
     * Test whether V-HACD should exploit multiple CPU cores (native field:
     * m_asyncACD).
     *
     * @return true &rarr; multiple cores, false &rarr; single core
     */
    public boolean isAsync() {
        long objectId = nativeId();
        boolean result = isAsync(objectId);

        return result;
    }

    /**
     * Test whether V-HACD should attempt to split planes at the best location
     * (native field: m_findBestPlane).
     *
     * @return true &rarr; multiple cores, false &rarr; single core
     */
    public boolean isFindBestPlane() {
        long objectId = nativeId();
        boolean result = isFindBestPlane(objectId);

        return result;
    }

    /**
     * Test whether V-HACD should shrinkwrap voxel positions to the source mesh
     * (native field: m_shrinkWrap).
     *
     * @return true &rarr; shrinkwrap, false &rarr; don't shrinkwrap
     */
    public boolean isShrinkWrap() {
        long objectId = nativeId();
        boolean result = isShrinkWrap(objectId);

        return result;
    }

    /**
     * Alter whether multiple CPU cores will be used (native field: m_asyncACD).
     *
     * @param setting true &rarr; multiple cores, false &rarr; single core
     * (default=true)
     */
    public void setAsync(boolean setting) {
        long objectId = nativeId();
        setAsync(objectId, setting);
    }

    /**
     * Alter whether debug output is enabled.
     *
     * @param setting true &rarr; enable, false &rarr; disable (default=false)
     */
    public void setDebugEnabled(boolean setting) {
        this.debug = setting;
    }

    /**
     * Specify the algorithm to generate a solid object from voxels (native
     * field: m_fillMode).
     *
     * @param mode the desired algorithm (not null, default=FloodFill)
     */
    public void setFillMode(FillMode mode) {
        Validate.nonNull(mode, "mode");

        long objectId = nativeId();
        int ordinal = mode.ordinal();
        setFillMode(objectId, ordinal);
    }

    /**
     * Alter whether to attempt to split planes at the best location (native
     * field: m_findBestPlane).
     *
     * @param setting true &rarr; attempt, false &rarr; don't attempt
     * (default=false)
     */
    public void setFindBestPlane(boolean setting) {
        long objectId = nativeId();
        setFindBestPlane(objectId, setting);
    }

    /**
     * Alter the maximum number of convex hulls (native field:
     * m_maxConvexHulls).
     *
     * @param limit default = 64, min = 1, max = 1024)
     */
    public void setMaxHulls(int limit) {
        Validate.inRange(limit, "limit", 1, 1024);

        long objectId = nativeId();
        setMaxHulls(objectId, limit);
    }

    /**
     * Alter the maximum recursion depth (native field: m_maxRecursionDepth).
     *
     * @param depth default = 14, min = 1, max = 1024
     */
    public void setMaxRecursion(int depth) {
        Validate.inRange(depth, "depth", 1, 1024);

        long objectId = nativeId();
        setMaxRecursion(objectId, depth);
    }

    /**
     * Alter the maximum number of vertices per convex hull (native field:
     * m_maxNumVerticesPerCH).
     * <p>
     * Note: the native default is 64.
     *
     * @param limit default = 32, min = 4, max = 1024)
     */
    public void setMaxVerticesPerHull(int limit) {
        Validate.inRange(limit, "limit", 4, 1024);

        long objectId = nativeId();
        setMaxNumVerticesPerCH(objectId, limit);
    }

    /**
     * Alter the minimum edge length (native field: m_minEdgeLength).
     *
     * @param length default = 2, min = 1, max = 1024)
     */
    public void setMinEdgeLength(int length) {
        Validate.inRange(length, "length", 1, 1024);

        long objectId = nativeId();
        setMinEdgeLength(objectId, length);
    }

    /**
     * Alter whether to shrinkwrap voxel positions to the source mesh (native
     * field: m_shrinkWrap).
     *
     * @param setting true &rarr; shrinkwrap, false &rarr; don't shrinkwrap
     * (default=true)
     */
    public void setShrinkWrap(boolean setting) {
        long objectId = nativeId();
        setShrinkWrap(objectId, setting);
    }

    /**
     * Alter the tolerance for the percent difference between the voxel volume
     * and the volume of the hull (native field:
     * m_minimumVolumePercentErrorAllowed).
     *
     * @param percentage default = 1, min = 0, max = 100
     */
    public void setVolumePercentError(double percentage) {
        Validate.inRange(percentage, "percentage", 0, 100);

        long objectId = nativeId();
        setVolumePercentError(objectId, percentage);
    }

    /**
     * Alter the maximum number of voxels generated during the voxelization
     * stage (native field: m_resolution).
     * <p>
     * Note: the native default is 400_000.
     *
     * @param maxVoxels default = 100_000, min = 10_000, max = 64_000_000
     */
    public void setVoxelResolution(int maxVoxels) {
        Validate.inRange(maxVoxels, "maxVoxels", 10_000, 64_000_000);

        long objectId = nativeId();
        setResolution(objectId, maxVoxels);
    }

    /**
     * Write all parameters to the specified OutputStream.
     *
     * @param os the stream to write (not null)
     * @throws IOException from DataOutputStream
     */
    public void toOutputStream(OutputStream os) throws IOException {
        DataOutputStream dos = new DataOutputStream(os);

        dos.writeBoolean(isAsync());
        dos.writeBoolean(getDebugEnabled());

        int fillModeOrdinal = getFillMode().ordinal();
        dos.writeInt(fillModeOrdinal);

        dos.writeBoolean(isFindBestPlane());
        dos.writeInt(getMaxHulls());
        dos.writeInt(getMaxRecursion());
        dos.writeInt(getMaxVerticesPerHull());
        dos.writeInt(getMinEdgeLength());
        dos.writeBoolean(isShrinkWrap());
        dos.writeDouble(getVolumePercentError());
        dos.writeInt(getVoxelResolution());
    }
    // *************************************************************************
    // NativePhysicsObject methods

    /**
     * Create a copy of these parameters.
     *
     * @return a new instance, equivalent to this one
     */
    @Override
    public Vhacd4Parameters clone() {
        try {
            Vhacd4Parameters clone = (Vhacd4Parameters) super.clone();
            long objectId = create();
            clone.reassignNativeId(objectId);

            clone.setAsync(isAsync());
            clone.setFillMode(getFillMode());
            clone.setFindBestPlane(isFindBestPlane());
            clone.setMaxHulls(getMaxHulls());
            clone.setMaxRecursion(getMaxRecursion());
            clone.setMaxVerticesPerHull(getMaxVerticesPerHull());
            clone.setMinEdgeLength(getMinEdgeLength());
            clone.setShrinkWrap(isShrinkWrap());
            clone.setVolumePercentError(getVolumePercentError());
            clone.setVoxelResolution(getVoxelResolution());

            return clone;

        } catch (CloneNotSupportedException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Test for exact equivalence with another Object.
     *
     * @param otherObject (may be null, unaffected)
     * @return true if the objects are equivalent, otherwise false
     */
    @Override
    public boolean equals(Object otherObject) {
        boolean result;
        if (otherObject == this) {
            result = true;
        } else if (otherObject != null
                && otherObject.getClass() == getClass()) {
            Vhacd4Parameters other = (Vhacd4Parameters) otherObject;
            result = isAsync() == other.isAsync()
                    && getDebugEnabled() == other.getDebugEnabled()
                    && getFillMode() == other.getFillMode()
                    && isFindBestPlane() == other.isFindBestPlane()
                    && getMaxHulls() == other.getMaxHulls()
                    && getMaxRecursion() == other.getMaxRecursion()
                    && getMaxVerticesPerHull() == other.getMaxVerticesPerHull()
                    && getMinEdgeLength() == other.getMinEdgeLength()
                    && isShrinkWrap() == other.isShrinkWrap()
                    && getVolumePercentError() == other.getVolumePercentError()
                    && getVoxelResolution() == other.getVoxelResolution();
        } else {
            result = false;
        }

        return result;
    }

    /**
     * Generate the hash code for this instance.
     *
     * @return a 32-bit value for use in hashing
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 2 * hash + (isAsync() ? 1 : 0);
        hash = 2 * hash + (getDebugEnabled() ? 1 : 0);
        hash = 3 * hash + getFillMode().ordinal();
        hash = 2 * hash + (isFindBestPlane() ? 1 : 0);
        hash = 83 * hash + getMaxHulls();
        hash = 83 * hash + getMaxRecursion();
        hash = 83 * hash + getMaxVerticesPerHull();
        hash = 83 * hash + getMinEdgeLength();
        hash = 2 * hash + (isShrinkWrap() ? 1 : 0);
        hash = 83 * hash + Double.hashCode(getVolumePercentError());
        hash = 83 * hash + getVoxelResolution();

        return hash;
    }
    // *************************************************************************
    // Java private methods

    /**
     * Free the identified tracked native object. Invoked by reflection.
     *
     * @param objectId the native identifier (not zero)
     */
    private static void freeNativeObject(long objectId) {
        assert objectId != 0L;
        finalizeNative(objectId);
    }
    // *************************************************************************
    // native private methods

    native private static long create();

    native private static void finalizeNative(long objectId);

    native private static int getFillMode(long objectId);

    native private static int getMaxHulls(long objectId);

    native private static int getMaxNumVerticesPerCH(long objectId);

    native private static int getMaxRecursion(long objectId);

    native private static int getMinEdgeLength(long objectId);

    native private static int getResolution(long objectId);

    native private static double getVolumePercentError(long objectId);

    native private static boolean isAsync(long objectId);

    native private static boolean isFindBestPlane(long objectId);

    native private static boolean isShrinkWrap(long objectId);

    native private static void setAsync(long objectId, boolean setting);

    native private static void setFillMode(long objectId, int ordinal);

    native private static void setFindBestPlane(long objectId, boolean setting);

    native private static void setMaxHulls(long objectId, int limit);

    native private static void setMaxNumVerticesPerCH(long objectId, int limit);

    native private static void setMaxRecursion(long objectId, int depth);

    native private static void setMinEdgeLength(long objectId, int length);

    native private static void setResolution(long objectId, int numVoxels);

    native private static void setShrinkWrap(long objectId, boolean setting);

    native private static void setVolumePercentError(
            long objectId, double percentage);
}