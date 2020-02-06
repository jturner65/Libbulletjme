/*
 * Copyright (c) 2009-2018 jMonkeyEngine
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
package com.jme3.util;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

import java.io.UnsupportedEncodingException;
import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <code>BufferUtils</code> is a helper class for generating nio buffers from
 * jME data classes such as Vectors.
 * 
 * @author Joshua Slack
 * @version $Id: BufferUtils.java,v 1.16 2007/10/29 16:56:18 nca Exp $
 */
public final class BufferUtils {

    /**
     * The field should be final to support thread-safe.
     */
    private static final BufferAllocator allocator = BufferAllocatorFactory.create();

    /**
     * Generate a new FloatBuffer using the given array of Vector3f objects. The
     * FloatBuffer will be 3 * data.length long and contain the vector data as
     * data[0].x, data[0].y, data[0].z, data[1].x... etc.
     * 
     * @param data
     *            array of Vector3f objects to place into a new FloatBuffer
     */
    public static FloatBuffer createFloatBuffer(Vector3f... data) {
        if (data == null) {
            return null;
        }
        FloatBuffer buff = createFloatBuffer(3 * data.length);
        for (Vector3f element : data) {
            if (element != null) {
                buff.put(element.x).put(element.y).put(element.z);
            } else {
                buff.put(0).put(0).put(0);
            }
        }
        buff.flip();
        return buff;
    }

    /**
     * Generate a new FloatBuffer using the given array of Quaternion objects.
     * The FloatBuffer will be 4 * data.length long and contain the vector data.
     * 
     * @param data
     *            array of Quaternion objects to place into a new FloatBuffer
     */
    public static FloatBuffer createFloatBuffer(Quaternion... data) {
        if (data == null) {
            return null;
        }
        FloatBuffer buff = createFloatBuffer(4 * data.length);
        for (Quaternion element : data) {
            if (element != null) {
                buff.put(element.getX()).put(element.getY()).put(element.getZ()).put(element.getW());
            } else {
                buff.put(0).put(0).put(0).put(0);
            }
        }
        buff.flip();
        return buff;
    }

    /**
     * Generate a new FloatBuffer using the given array of float primitives.
     * 
     * @param data
     *            array of float primitives to place into a new FloatBuffer
     */
    public static FloatBuffer createFloatBuffer(float... data) {
        if (data == null) {
            return null;
        }
        FloatBuffer buff = createFloatBuffer(data.length);
        buff.clear();
        buff.put(data);
        buff.flip();
        return buff;
    }

    /**
     * Create a new FloatBuffer of an appropriate size to hold the specified
     * number of Vector3f object data.
     * 
     * @param vertices
     *            number of vertices that need to be held by the newly created
     *            buffer
     * @return the requested new FloatBuffer
     */
    public static FloatBuffer createVector3Buffer(int vertices) {
        FloatBuffer vBuff = createFloatBuffer(3 * vertices);
        return vBuff;
    }

    /**
     * Create a new FloatBuffer of an appropriate size to hold the specified
     * number of Vector3f object data only if the given buffer if not already
     * the right size.
     * 
     * @param buf
     *            the buffer to first check and rewind
     * @param vertices
     *            number of vertices that need to be held by the newly created
     *            buffer
     * @return the requested new FloatBuffer
     */
    public static FloatBuffer createVector3Buffer(FloatBuffer buf, int vertices) {
        if (buf != null && buf.limit() == 3 * vertices) {
            buf.rewind();
            return buf;
        }

        return createFloatBuffer(3 * vertices);
    }

    /**
     * Sets the data contained in the given quaternion into the FloatBuffer at
     * the specified index.
     * 
     * @param quat
     *            the {@link Quaternion} to insert
     * @param buf
     *            the buffer to insert into
     * @param index
     *            the position to place the data; in terms of quaternions not
     *            floats
     */
    public static void setInBuffer(Quaternion quat, FloatBuffer buf, int index) {
        buf.position(index * 4);
        buf.put(quat.getX());
        buf.put(quat.getY());
        buf.put(quat.getZ());
        buf.put(quat.getW());
    }

    /**
     * Sets the data contained in the given Vector3F into the FloatBuffer at the
     * specified index.
     * 
     * @param vector
     *            the data to insert
     * @param buf
     *            the buffer to insert into
     * @param index
     *            the position to place the data; in terms of vectors not floats
     */
    public static void setInBuffer(Vector3f vector, FloatBuffer buf, int index) {
        if (buf == null) {
            return;
        }
        if (vector == null) {
            buf.put(index * 3, 0);
            buf.put((index * 3) + 1, 0);
            buf.put((index * 3) + 2, 0);
        } else {
            buf.put(index * 3, vector.x);
            buf.put((index * 3) + 1, vector.y);
            buf.put((index * 3) + 2, vector.z);
        }
    }

    /**
     * Updates the values of the given vector from the specified buffer at the
     * index provided.
     * 
     * @param vector
     *            the vector to set data on
     * @param buf
     *            the buffer to read from
     * @param index
     *            the position (in terms of vectors, not floats) to read from
     *            the buf
     */
    public static void populateFromBuffer(Vector3f vector, FloatBuffer buf, int index) {
        vector.x = buf.get(index * 3);
        vector.y = buf.get(index * 3 + 1);
        vector.z = buf.get(index * 3 + 2);
    }

    /**
     * Generates a Vector3f array from the given FloatBuffer.
     * 
     * @param buff
     *            the FloatBuffer to read from
     * @return a newly generated array of Vector3f objects
     */
    public static Vector3f[] getVector3Array(FloatBuffer buff) {
        buff.clear();
        Vector3f[] verts = new Vector3f[buff.limit() / 3];
        for (int x = 0; x < verts.length; x++) {
            Vector3f v = new Vector3f(buff.get(), buff.get(), buff.get());
            verts[x] = v;
        }
        return verts;
    }

    /**
     * Copies a Vector3f from one position in the buffer to another. The index
     * values are in terms of vector number (eg, vector number 0 is positions
     * 0-2 in the FloatBuffer.)
     * 
     * @param buf
     *            the buffer to copy from/to
     * @param fromPos
     *            the index of the vector to copy
     * @param toPos
     *            the index to copy the vector to
     */
    public static void copyInternalVector3(FloatBuffer buf, int fromPos, int toPos) {
        copyInternal(buf, fromPos * 3, toPos * 3, 3);
    }

    /**
     * Normalize a Vector3f in-buffer.
     * 
     * @param buf
     *            the buffer to find the Vector3f within
     * @param index
     *            the position (in terms of vectors, not floats) of the vector
     *            to normalize
     */
    public static void normalizeVector3(FloatBuffer buf, int index) {
        TempVars vars = TempVars.get();
        Vector3f tempVec3 = vars.vect1;
        populateFromBuffer(tempVec3, buf, index);
        tempVec3.normalizeLocal();
        setInBuffer(tempVec3, buf, index);
        vars.release();
    }

    /**
     * Add to a Vector3f in-buffer.
     * 
     * @param toAdd
     *            the vector to add from
     * @param buf
     *            the buffer to find the Vector3f within
     * @param index
     *            the position (in terms of vectors, not floats) of the vector
     *            to add to
     */
    public static void addInBuffer(Vector3f toAdd, FloatBuffer buf, int index) {
        TempVars vars = TempVars.get();
        Vector3f tempVec3 = vars.vect1;
        populateFromBuffer(tempVec3, buf, index);
        tempVec3.addLocal(toAdd);
        setInBuffer(tempVec3, buf, index);
        vars.release();
    }

    /**
     * Multiply and store a Vector3f in-buffer.
     * 
     * @param toMult
     *            the vector to multiply against
     * @param buf
     *            the buffer to find the Vector3f within
     * @param index
     *            the position (in terms of vectors, not floats) of the vector
     *            to multiply
     */
    public static void multInBuffer(Vector3f toMult, FloatBuffer buf, int index) {
        TempVars vars = TempVars.get();
        Vector3f tempVec3 = vars.vect1;
        populateFromBuffer(tempVec3, buf, index);
        tempVec3.multLocal(toMult);
        setInBuffer(tempVec3, buf, index);
        vars.release();
    }

    /**
     * Checks to see if the given Vector3f is equals to the data stored in the
     * buffer at the given data index.
     * 
     * @param check
     *            the vector to check against - null will return false.
     * @param buf
     *            the buffer to compare data with
     * @param index
     *            the position (in terms of vectors, not floats) of the vector
     *            in the buffer to check against
     * @return true if the data is equivalent, otherwise false.
     */
    public static boolean equals(Vector3f check, FloatBuffer buf, int index) {
        TempVars vars = TempVars.get();
        Vector3f tempVec3 = vars.vect1;
        populateFromBuffer(tempVec3, buf, index);
        boolean eq = tempVec3.equals(check);
        vars.release();
        return eq;
    }

    //// -- INT METHODS -- ////
    /**
     * Generate a new IntBuffer using the given array of ints. The IntBuffer
     * will be data.length long and contain the int data as data[0], data[1]...
     * etc.
     * 
     * @param data
     *            array of ints to place into a new IntBuffer
     */
    public static IntBuffer createIntBuffer(int... data) {
        if (data == null) {
            return null;
        }
        IntBuffer buff = createIntBuffer(data.length);
        buff.clear();
        buff.put(data);
        buff.flip();
        return buff;
    }

    /**
     * Create a new int[] array and populate it with the given IntBuffer's
     * contents.
     * 
     * @param buff
     *            the IntBuffer to read from
     * @return a new int array populated from the IntBuffer
     */
    public static int[] getIntArray(IntBuffer buff) {
        if (buff == null) {
            return null;
        }
        buff.clear();
        int[] inds = new int[buff.limit()];
        for (int x = 0; x < inds.length; x++) {
            inds[x] = buff.get();
        }
        return inds;
    }

    /**
     * Create a new float[] array and populate it with the given FloatBuffer's
     * contents.
     * 
     * @param buff
     *            the FloatBuffer to read from
     * @return a new float array populated from the FloatBuffer
     */
    public static float[] getFloatArray(FloatBuffer buff) {
        if (buff == null) {
            return null;
        }
        buff.clear();
        float[] inds = new float[buff.limit()];
        for (int x = 0; x < inds.length; x++) {
            inds[x] = buff.get();
        }
        return inds;
    }

    //// -- GENERAL FLOAT ROUTINES -- ////
    /**
     * Create a new FloatBuffer of the specified size.
     * 
     * @param size
     *            required number of floats to store.
     * @return the new FloatBuffer
     */
    public static FloatBuffer createFloatBuffer(int size) {
        FloatBuffer buf = allocator.allocate(4 * size).order(ByteOrder.nativeOrder()).asFloatBuffer();
        buf.clear();
        return buf;
    }

    /**
     * Copies floats from one position in the buffer to another.
     * 
     * @param buf
     *            the buffer to copy from/to
     * @param fromPos
     *            the starting point to copy from
     * @param toPos
     *            the starting point to copy to
     * @param length
     *            the number of floats to copy
     */
    public static void copyInternal(FloatBuffer buf, int fromPos, int toPos, int length) {
        float[] data = new float[length];
        buf.position(fromPos);
        buf.get(data);
        buf.position(toPos);
        buf.put(data);
    }

    //// -- GENERAL INT ROUTINES -- ////
    /**
     * Create a new IntBuffer of the specified size.
     * 
     * @param size
     *            required number of ints to store.
     * @return the new IntBuffer
     */
    public static IntBuffer createIntBuffer(int size) {
        IntBuffer buf = allocator.allocate(4 * size).order(ByteOrder.nativeOrder()).asIntBuffer();
        buf.clear();
        return buf;
    }

    /**
     * Create a new IntBuffer of an appropriate size to hold the specified
     * number of ints only if the given buffer if not already the right size.
     * 
     * @param buf
     *            the buffer to first check and rewind
     * @param size
     *            number of ints that need to be held by the newly created
     *            buffer
     * @return the requested new IntBuffer
     */
    public static IntBuffer createIntBuffer(IntBuffer buf, int size) {
        if (buf != null && buf.limit() == size) {
            buf.rewind();
            return buf;
        }

        buf = createIntBuffer(size);
        return buf;
    }

    //// -- GENERAL BYTE ROUTINES -- ////
    /**
     * Create a new ByteBuffer of the specified size.
     * 
     * @param size
     *            required number of ints to store.
     * @return the new IntBuffer
     */
    public static ByteBuffer createByteBuffer(int size) {
        ByteBuffer buf = allocator.allocate(size).order(ByteOrder.nativeOrder());
        buf.clear();
        return buf;
    }

    /**
     * Create a new ByteBuffer of an appropriate size to hold the specified
     * number of ints only if the given buffer if not already the right size.
     * 
     * @param buf
     *            the buffer to first check and rewind
     * @param size
     *            number of bytes that need to be held by the newly created
     *            buffer
     * @return the requested new IntBuffer
     */
    public static ByteBuffer createByteBuffer(ByteBuffer buf, int size) {
        if (buf != null && buf.limit() == size) {
            buf.rewind();
            return buf;
        }

        buf = createByteBuffer(size);
        return buf;
    }

    public static ByteBuffer createByteBuffer(byte... data) {
        ByteBuffer bb = createByteBuffer(data.length);
        bb.put(data);
        bb.flip();
        return bb;
    }

    public static ByteBuffer createByteBuffer(String data) {
        try {
            byte[] bytes = data.getBytes("UTF-8");
            ByteBuffer bb = createByteBuffer(bytes.length);
            bb.put(bytes);
            bb.flip();
            return bb;
        } catch (UnsupportedEncodingException ex) {
            throw new UnsupportedOperationException(ex);
        }
    }

    //// -- GENERAL SHORT ROUTINES -- ////
    /**
     * Create a new ShortBuffer of the specified size.
     * 
     * @param size
     *            required number of shorts to store.
     * @return the new ShortBuffer
     */
    public static ShortBuffer createShortBuffer(int size) {
        ShortBuffer buf = allocator.allocate(2 * size).order(ByteOrder.nativeOrder()).asShortBuffer();
        buf.clear();
        return buf;
    }

    /**
     * Create a new ShortBuffer of an appropriate size to hold the specified
     * number of shorts only if the given buffer if not already the right size.
     * 
     * @param buf
     *            the buffer to first check and rewind
     * @param size
     *            number of shorts that need to be held by the newly created
     *            buffer
     * @return the requested new ShortBuffer
     */
    public static ShortBuffer createShortBuffer(ShortBuffer buf, int size) {
        if (buf != null && buf.limit() == size) {
            buf.rewind();
            return buf;
        }

        buf = createShortBuffer(size);
        return buf;
    }

    public static ShortBuffer createShortBuffer(short... data) {
        if (data == null) {
            return null;
        }
        ShortBuffer buff = createShortBuffer(data.length);
        buff.clear();
        buff.put(data);
        buff.flip();
        return buff;
    }

    /**
     * Ensures there is at least the <code>required</code> number of entries
     * left after the current position of the buffer. If the buffer is too small
     * a larger one is created and the old one copied to the new buffer.
     * 
     * @param buffer
     *            buffer that should be checked/copied (may be null)
     * @param required
     *            minimum number of elements that should be remaining in the
     *            returned buffer
     * @return a buffer large enough to receive at least the
     *         <code>required</code> number of entries, same position as the
     *         input buffer, not null
     */
    public static FloatBuffer ensureLargeEnough(FloatBuffer buffer, int required) {
        if (buffer != null) {
            buffer.limit(buffer.capacity());
        }
        if (buffer == null || (buffer.remaining() < required)) {
            int position = (buffer != null ? buffer.position() : 0);
            FloatBuffer newVerts = createFloatBuffer(position + required);
            if (buffer != null) {
                buffer.flip();
                newVerts.put(buffer);
                newVerts.position(position);
            }
            buffer = newVerts;
        }
        return buffer;
    }

    public static IntBuffer ensureLargeEnough(IntBuffer buffer, int required) {
        if (buffer != null) {
            buffer.limit(buffer.capacity());
        }
        if (buffer == null || (buffer.remaining() < required)) {
            int position = (buffer != null ? buffer.position() : 0);
            IntBuffer newVerts = createIntBuffer(position + required);
            if (buffer != null) {
                buffer.flip();
                newVerts.put(buffer);
                newVerts.position(position);
            }
            buffer = newVerts;
        }
        return buffer;
    }

    public static ShortBuffer ensureLargeEnough(ShortBuffer buffer, int required) {
        if (buffer != null) {
            buffer.limit(buffer.capacity());
        }
        if (buffer == null || (buffer.remaining() < required)) {
            int position = (buffer != null ? buffer.position() : 0);
            ShortBuffer newVerts = createShortBuffer(position + required);
            if (buffer != null) {
                buffer.flip();
                newVerts.put(buffer);
                newVerts.position(position);
            }
            buffer = newVerts;
        }
        return buffer;
    }

    public static ByteBuffer ensureLargeEnough(ByteBuffer buffer, int required) {
        if (buffer != null) {
            buffer.limit(buffer.capacity());
        }
        if (buffer == null || (buffer.remaining() < required)) {
            int position = (buffer != null ? buffer.position() : 0);
            ByteBuffer newVerts = createByteBuffer(position + required);
            if (buffer != null) {
                buffer.flip();
                newVerts.put(buffer);
                newVerts.position(position);
            }
            buffer = newVerts;
        }
        return buffer;
    }
}
