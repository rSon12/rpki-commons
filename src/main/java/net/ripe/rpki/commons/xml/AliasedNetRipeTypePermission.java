/**
 * The BSD License
 *
 * Copyright (c) 2010-2021 RIPE NCC
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *   - Redistributions of source code must retain the above copyright notice,
 *     this list of conditions and the following disclaimer.
 *   - Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *   - Neither the name of the RIPE NCC nor the names of its contributors may be
 *     used to endorse or promote products derived from this software without
 *     specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package net.ripe.rpki.commons.xml;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.TypePermission;

/**
 * XStream TypePermission that accepts all types that have been aliased that are from net.ripe.
 *
 * Prevents types for which a default alias exists, that are not on the allow-list, and not from net.ripe.
 * to be de-serialised. Further limits the amount of classes that are available to be used as gadgets.
 */
public class AliasedNetRipeTypePermission implements TypePermission {
    private final XStream xStream;

    public AliasedNetRipeTypePermission(XStream xStream) {
        this.xStream = xStream;
    }

    /**
     * Allow types that have an alias by checking whether their serialized name differs from their
     * fully qualified name.
     *
     * @param type type to check
     * @return whether an alias has been applied to the type
     */
    @Override
    public boolean allows(Class type) {
        return type.getName().startsWith("net.ripe.") && !type.getName().equals(xStream.getMapper().serializedClass(type));
    }
}
