/*
 * Copyright (c) 2000, 2016, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package java.nio;

// ByteBuffer转为CharBuffer，使用只读缓冲区，是ByteBufferAsCharBufferB的只读版本
class ByteBufferAsCharBufferRB extends ByteBufferAsCharBufferB {
    
    /*▼ 构造方法 ████████████████████████████████████████████████████████████████████████████████┓ */
    
    ByteBufferAsCharBufferRB(ByteBuffer bb) {
        super(bb);
    }
    
    ByteBufferAsCharBufferRB(ByteBuffer bb, int mark, int pos, int lim, int cap, long addr) {
        super(bb, mark, pos, lim, cap, addr);
    }
    
    /*▲ 构造方法 ████████████████████████████████████████████████████████████████████████████████┛ */
    
    
    
    /*▼ 只读缓冲区 ████████████████████████████████████████████████████████████████████████████████┓ */
    
    public boolean isReadOnly() {
        return true;
    }
    
    public boolean isDirect() {
        return bb.isDirect();
    }
    
    /*▲ 只读缓冲区 ████████████████████████████████████████████████████████████████████████████████┛ */
    
    
    
    /*▼ 创建新缓冲区，新旧缓冲区共享内部的存储容器 ████████████████████████████████████████████████████████████████████████████████┓ */
    
    public CharBuffer slice() {
        int pos = this.position();
        int lim = this.limit();
        assert (pos <= lim);
        int rem = (pos <= lim ? lim - pos : 0);
        long addr = byteOffset(pos);
        return new ByteBufferAsCharBufferRB(bb, -1, 0, rem, rem, addr);
    }
    
    public CharBuffer duplicate() {
        return new ByteBufferAsCharBufferRB(bb, this.markValue(), this.position(), this.limit(), this.capacity(), address);
    }
    
    public CharBuffer asReadOnlyBuffer() {
        return duplicate();
    }
    
    public CharBuffer subSequence(int start, int end) {
        int pos = position();
        int lim = limit();
        assert (pos <= lim);
        pos = (pos <= lim ? pos : lim);
        int len = lim - pos;
        
        if((start < 0) || (end > len) || (start > end))
            throw new IndexOutOfBoundsException();
        return new ByteBufferAsCharBufferRB(bb, -1, pos + start, pos + end, capacity(), address);
    }
    
    /*▲ 创建新缓冲区，新旧缓冲区共享内部的存储容器 ████████████████████████████████████████████████████████████████████████████████┛ */
    
    
    
    /*▼ 只读缓冲区，禁止写入 ████████████████████████████████████████████████████████████████████████████████┓ */
    
    public CharBuffer put(char x) {
        throw new ReadOnlyBufferException();
    }
    
    public CharBuffer put(int i, char x) {
        throw new ReadOnlyBufferException();
    }
    
    /*▲ 只读缓冲区，禁止写入 ████████████████████████████████████████████████████████████████████████████████┛ */
    
    
    
    /*▼ 禁止压缩，因为禁止写入，压缩没意义 ████████████████████████████████████████████████████████████████████████████████┓ */
    
    public CharBuffer compact() {
        throw new ReadOnlyBufferException();
    }
    
    /*▲ 禁止压缩，因为禁止写入，压缩没意义 ████████████████████████████████████████████████████████████████████████████████┛ */
    
    
    
    /*▼ 字节顺序 ████████████████████████████████████████████████████████████████████████████████┓ */
    
    public ByteOrder order() {
        return ByteOrder.BIG_ENDIAN;
    }
    
    ByteOrder charRegionOrder() {
        return order();
    }
    
    /*▲ 字节顺序 ████████████████████████████████████████████████████████████████████████████████┛ */
    
    
    
    @Override
    Object base() {
        return bb.hb;
    }
    
    public String toString(int start, int end) {
        if((end > limit()) || (start > end))
            throw new IndexOutOfBoundsException();
        try {
            int len = end - start;
            char[] ca = new char[len];
            CharBuffer cb = CharBuffer.wrap(ca);
            CharBuffer db = this.duplicate();
            db.position(start);
            db.limit(end);
            cb.put(db);
            return new String(ca);
        } catch(StringIndexOutOfBoundsException x) {
            throw new IndexOutOfBoundsException();
        }
    }
}
