/*
 * Copyright © 2016 <code@io7m.com> http://io7m.com
 * 
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
 * IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package com.io7m.jcamera.tests;

import com.io7m.jcamera.JCameraSignallingClamp;
import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("static-method")
public final class JCameraSignallingClampTest
{
  @Test
  public void testClampMaximum()
  {
    final JCameraSignallingClamp sc = new JCameraSignallingClamp();
    sc.clamp(2.0f, 0.0f, 1.0f);
    Assert.assertEquals(1.0f, sc.getValue(), 0.0f);
    Assert.assertTrue(sc.isClamped());
  }

  @Test
  public void testClampMinimum()
  {
    final JCameraSignallingClamp sc = new JCameraSignallingClamp();
    sc.clamp(-1.0f, 0.0f, 1.0f);
    Assert.assertEquals(0.0f, sc.getValue(), 0.0f);
    Assert.assertTrue(sc.isClamped());
  }

  @Test
  public void testNoClamp()
  {
    final JCameraSignallingClamp sc = new JCameraSignallingClamp();
    sc.clamp(0.5f, 0.0f, 1.0f);
    Assert.assertEquals(0.5f, sc.getValue(), 0.0f);
    Assert.assertFalse(sc.isClamped());
  }
}
