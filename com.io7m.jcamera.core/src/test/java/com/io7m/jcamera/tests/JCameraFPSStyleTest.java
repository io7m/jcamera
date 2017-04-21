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

import com.io7m.jcamera.JCameraFPSStyle;
import com.io7m.jcamera.JCameraFPSStyleReadableType;
import com.io7m.jcamera.JCameraFPSStyleSnapshot;
import com.io7m.jcamera.JCameraFPSStyleSnapshots;
import com.io7m.jcamera.JCameraFPSStyleType;
import com.io7m.jtensors.core.parameterized.matrices.PMatrix4x4D;
import com.io7m.jtensors.core.unparameterized.matrices.Matrix4x4D;
import com.io7m.jtensors.core.unparameterized.vectors.Vector3D;
import org.junit.Assert;
import org.junit.Test;

import static com.io7m.jequality.AlmostEqualDouble.ContextRelative;
import static com.io7m.jequality.AlmostEqualDouble.almostEqual;

public final class JCameraFPSStyleTest
{
  private static void dumpVector(
    final String name,
    final Vector3D v)
  {
    System.out.printf(
      "%-18s : %f %f %f\n",
      name,
      Double.valueOf(v.x()),
      Double.valueOf(v.y()),
      Double.valueOf(v.z()));
  }

  private void compareSnapshot(
    final JCameraFPSStyleReadableType c)
  {
    final JCameraFPSStyleSnapshot snap = JCameraFPSStyleSnapshots.of(c);
    Assert.assertEquals(
      snap.cameraGetAngleAroundHorizontal(),
      c.cameraGetAngleAroundHorizontal(),
      0.0);
    Assert.assertEquals(
      snap.cameraGetAngleAroundVertical(),
      c.cameraGetAngleAroundVertical(),
      0.0);
    this.compareVector(snap.cameraGetForward(), c.cameraGetForward());
    this.compareVector(snap.cameraGetRight(), c.cameraGetRight());
    this.compareVector(snap.cameraGetUp(), c.cameraGetUp());
    this.compareVector(snap.cameraGetPosition(), c.cameraGetPosition());

    final Matrix4x4D m = c.cameraMakeViewMatrix();
    final PMatrix4x4D<Object, Object> pm = c.cameraMakeViewPMatrix();

    final Matrix4x4D snap_m = snap.cameraMakeViewMatrix();
    final PMatrix4x4D<Object, Object> snap_pm = snap.cameraMakeViewPMatrix();

    Assert.assertEquals(m, snap_m);
    Assert.assertEquals(pm, snap_pm);

    final JCameraFPSStyleSnapshot snap2 = JCameraFPSStyleSnapshots.of(c);
    final JCameraFPSStyleSnapshot snap3 = JCameraFPSStyleSnapshots.of(snap);

    Assert.assertEquals(snap, snap2);
    Assert.assertEquals(snap2, snap3);
    Assert.assertEquals(snap, snap3);
  }

  private void compareVector(
    final Vector3D a,
    final Vector3D b)
  {
    Assert.assertEquals(a.x(), b.x(), 0.0);
    Assert.assertEquals(a.y(), b.y(), 0.0);
    Assert.assertEquals(a.z(), b.z(), 0.0);
  }

  @Test
  public void testClampHorizontal_0()
  {
    final JCameraFPSStyleType c0 = JCameraFPSStyle.newCamera();
    c0.cameraClampHorizontalEnable(-2.0, 2.0);
    c0.cameraSetAngleAroundHorizontal(-10.0);
    Assert.assertEquals(-2.0, c0.cameraGetAngleAroundHorizontal(), 0.0);
  }

  @Test
  public void testClampHorizontal_1()
  {
    final JCameraFPSStyleType c0 = JCameraFPSStyle.newCamera();
    c0.cameraClampHorizontalEnable(-2.0, 2.0);
    c0.cameraSetAngleAroundHorizontal(10.0);
    Assert.assertEquals(2.0, c0.cameraGetAngleAroundHorizontal(), 0.0);
  }

  @Test
  public void testClampHorizontal_2()
  {
    final JCameraFPSStyleType c0 = JCameraFPSStyle.newCamera();
    c0.cameraClampHorizontalDisable();
    c0.cameraSetAngleAroundHorizontal(10.0);
    Assert.assertEquals(10.0, c0.cameraGetAngleAroundHorizontal(), 0.0);
  }

  @Test
  public void testDirectionsHorizontal()
  {
    final JCameraFPSStyleType c = JCameraFPSStyle.newCamera();

    for (double index = 0.0; index < 360.0; index += 0.1) {
      c.cameraRotateAroundHorizontal(0.1);
      this.compareSnapshot(c);
    }
  }

  @Test
  public void testDirectionsInitial()
  {
    final JCameraFPSStyleReadableType c = JCameraFPSStyle.newCamera();

    System.out.println("-- initial");

    {
      final Vector3D expected = Vector3D.of(0.0, 0.0, -1.0);
      final Vector3D actual = c.cameraGetForward();
      dumpVector("forward expected", expected);
      dumpVector("forward actual", actual);
      Assert.assertEquals(expected.x(), actual.x(), 0.00001);
      Assert.assertEquals(expected.y(), actual.y(), 0.00001);
      Assert.assertEquals(expected.z(), actual.z(), 0.00001);
    }

    {
      final Vector3D expected = Vector3D.of(1.0, 0.0, 0.0);
      final Vector3D actual = c.cameraGetRight();
      dumpVector("right expected", expected);
      dumpVector("right actual", actual);
      Assert.assertEquals(expected.x(), actual.x(), 0.00001);
      Assert.assertEquals(expected.y(), actual.y(), 0.00001);
      Assert.assertEquals(expected.z(), actual.z(), 0.00001);
    }

    {
      final Vector3D expected = Vector3D.of(0.0, 1.0, 0.0);
      final Vector3D actual = c.cameraGetUp();
      dumpVector("up expected", expected);
      dumpVector("up actual", actual);
      Assert.assertEquals(expected.x(), actual.x(), 0.00001);
      Assert.assertEquals(expected.y(), actual.y(), 0.00001);
      Assert.assertEquals(expected.z(), actual.z(), 0.00001);
    }

    this.compareSnapshot(c);
  }

  @Test
  public void testDirectionsTurnDown45()
  {
    final JCameraFPSStyleType c = JCameraFPSStyle.newCamera();

    System.out.println("-- down 45");

    c.cameraRotateAroundHorizontal(Math.toRadians(-45.0));

    {
      final Vector3D expected = Vector3D.of(0.0, -0.707107, -0.707107);
      final Vector3D actual = c.cameraGetForward();
      dumpVector("forward expected", expected);
      dumpVector("forward actual", actual);
      Assert.assertEquals(expected.x(), actual.x(), 0.00001);
      Assert.assertEquals(expected.y(), actual.y(), 0.00001);
      Assert.assertEquals(expected.z(), actual.z(), 0.00001);
    }

    {
      final Vector3D expected = Vector3D.of(1.0, 0.0, 0.0);
      final Vector3D actual = c.cameraGetRight();
      dumpVector("right expected", expected);
      dumpVector("right actual", actual);
      Assert.assertEquals(expected.x(), actual.x(), 0.00001);
      Assert.assertEquals(expected.y(), actual.y(), 0.00001);
      Assert.assertEquals(expected.z(), actual.z(), 0.00001);
    }

    {
      final Vector3D expected = Vector3D.of(0.0, 0.707107, -0.707107);
      final Vector3D actual = c.cameraGetUp();
      dumpVector("up expected", expected);
      dumpVector("up actual", actual);
      Assert.assertEquals(expected.x(), actual.x(), 0.00001);
      Assert.assertEquals(expected.y(), actual.y(), 0.00001);
      Assert.assertEquals(expected.z(), actual.z(), 0.00001);
    }

    this.compareSnapshot(c);
  }

  @Test
  public void testDirectionsTurnDown90()
  {
    final JCameraFPSStyleType c = JCameraFPSStyle.newCamera();

    System.out.println("-- down 90");

    c.cameraClampHorizontalDisable();
    c.cameraRotateAroundHorizontal(Math.toRadians(-89.999));

    {
      final Vector3D expected = Vector3D.of(0.0, -1.0, -0.00001);
      final Vector3D actual = c.cameraGetForward();
      dumpVector("forward expected", expected);
      dumpVector("forward actual", actual);
      Assert.assertEquals(expected.x(), actual.x(), 0.00001);
      Assert.assertEquals(expected.y(), actual.y(), 0.00001);
      Assert.assertEquals(expected.z(), actual.z(), 0.00001);
    }

    {
      final Vector3D expected = Vector3D.of(1.0, 0.0, 0.0);
      final Vector3D actual = c.cameraGetRight();
      dumpVector("right expected", expected);
      dumpVector("right actual", actual);
      Assert.assertEquals(expected.x(), actual.x(), 0.00001);
      Assert.assertEquals(expected.y(), actual.y(), 0.00001);
      Assert.assertEquals(expected.z(), actual.z(), 0.00001);
    }

    {
      final Vector3D expected = Vector3D.of(0.0, 0.00001, -1.0);
      final Vector3D actual = c.cameraGetUp();
      dumpVector("up expected", expected);
      dumpVector("up actual", actual);
      Assert.assertEquals(expected.x(), actual.x(), 0.00001);
      Assert.assertEquals(expected.y(), actual.y(), 0.00001);
      Assert.assertEquals(expected.z(), actual.z(), 0.00001);
    }

    this.compareSnapshot(c);
  }

  @Test
  public void testDirectionsTurnLeft90()
  {
    final JCameraFPSStyleType c = JCameraFPSStyle.newCamera();

    System.out.println("-- left 90");

    c.cameraRotateAroundVertical(Math.toRadians(90.0));

    {
      final Vector3D expected = Vector3D.of(-1.0, 0.0, 0.0);
      final Vector3D actual = c.cameraGetForward();
      dumpVector("forward expected", expected);
      dumpVector("forward actual", actual);
      Assert.assertEquals(expected.x(), actual.x(), 0.00001);
      Assert.assertEquals(expected.y(), actual.y(), 0.00001);
      Assert.assertEquals(expected.z(), actual.z(), 0.00001);
    }

    {
      final Vector3D expected = Vector3D.of(0.0, 0.0, -1.0);
      final Vector3D actual = c.cameraGetRight();
      dumpVector("right expected", expected);
      dumpVector("right actual", actual);
      Assert.assertEquals(expected.x(), actual.x(), 0.00001);
      Assert.assertEquals(expected.y(), actual.y(), 0.00001);
      Assert.assertEquals(expected.z(), actual.z(), 0.00001);
    }

    {
      final Vector3D expected = Vector3D.of(0.0, 1.0, 0.0);
      final Vector3D actual = c.cameraGetUp();
      dumpVector("up expected", expected);
      dumpVector("up actual", actual);
      Assert.assertEquals(expected.x(), actual.x(), 0.00001);
      Assert.assertEquals(expected.y(), actual.y(), 0.00001);
      Assert.assertEquals(expected.z(), actual.z(), 0.00001);
    }

    this.compareSnapshot(c);
  }

  @Test
  public void testDirectionsTurnRight180()
  {
    final JCameraFPSStyleType c = JCameraFPSStyle.newCamera();

    System.out.println("-- right 180");

    c.cameraRotateAroundVertical(Math.toRadians(-180.0));

    {
      final Vector3D expected = Vector3D.of(0.0, 0.0, 1.0);
      final Vector3D actual = c.cameraGetForward();
      dumpVector("forward expected", expected);
      dumpVector("forward actual", actual);
      Assert.assertEquals(expected.x(), actual.x(), 0.00001);
      Assert.assertEquals(expected.y(), actual.y(), 0.00001);
      Assert.assertEquals(expected.z(), actual.z(), 0.00001);
    }

    {
      final Vector3D expected = Vector3D.of(-1.0, 0.0, 0.0);
      final Vector3D actual = c.cameraGetRight();
      dumpVector("right expected", expected);
      dumpVector("right actual", actual);
      Assert.assertEquals(expected.x(), actual.x(), 0.00001);
      Assert.assertEquals(expected.y(), actual.y(), 0.00001);
      Assert.assertEquals(expected.z(), actual.z(), 0.00001);
    }

    {
      final Vector3D expected = Vector3D.of(0.0, 1.0, 0.0);
      final Vector3D actual = c.cameraGetUp();
      dumpVector("up expected", expected);
      dumpVector("up actual", actual);
      Assert.assertEquals(expected.x(), actual.x(), 0.00001);
      Assert.assertEquals(expected.y(), actual.y(), 0.00001);
      Assert.assertEquals(expected.z(), actual.z(), 0.00001);
    }

    this.compareSnapshot(c);
  }

  @Test
  public void testDirectionsTurnRight270()
  {
    final JCameraFPSStyleType c = JCameraFPSStyle.newCamera();

    System.out.println("-- right 270");

    c.cameraRotateAroundVertical(Math.toRadians(-270.0));

    {
      final Vector3D expected = Vector3D.of(-1.0, 0.0, 0.0);
      final Vector3D actual = c.cameraGetForward();
      dumpVector("forward expected", expected);
      dumpVector("forward actual", actual);
      Assert.assertEquals(expected.x(), actual.x(), 0.00001);
      Assert.assertEquals(expected.y(), actual.y(), 0.00001);
      Assert.assertEquals(expected.z(), actual.z(), 0.00001);
    }

    {
      final Vector3D expected = Vector3D.of(0.0, 0.0, -1.0);
      final Vector3D actual = c.cameraGetRight();
      dumpVector("right expected", expected);
      dumpVector("right actual", actual);
      Assert.assertEquals(expected.x(), actual.x(), 0.00001);
      Assert.assertEquals(expected.y(), actual.y(), 0.00001);
      Assert.assertEquals(expected.z(), actual.z(), 0.00001);
    }

    {
      final Vector3D expected = Vector3D.of(0.0, 1.0, 0.0);
      final Vector3D actual = c.cameraGetUp();
      dumpVector("up expected", expected);
      dumpVector("up actual", actual);
      Assert.assertEquals(expected.x(), actual.x(), 0.00001);
      Assert.assertEquals(expected.y(), actual.y(), 0.00001);
      Assert.assertEquals(expected.z(), actual.z(), 0.00001);
    }

    this.compareSnapshot(c);
  }

  @Test
  public void testDirectionsTurnRight90()
  {
    final JCameraFPSStyleType c = JCameraFPSStyle.newCamera();

    System.out.println("-- right 90");

    c.cameraRotateAroundVertical(Math.toRadians(-90.0));

    {
      final Vector3D expected = Vector3D.of(1.0, 0.0, 0.0);
      final Vector3D actual = c.cameraGetForward();
      dumpVector("forward expected", expected);
      dumpVector("forward actual", actual);
      Assert.assertEquals(expected.x(), actual.x(), 0.00001);
      Assert.assertEquals(expected.y(), actual.y(), 0.00001);
      Assert.assertEquals(expected.z(), actual.z(), 0.00001);
    }

    {
      final Vector3D expected = Vector3D.of(0.0, 0.0, 1.0);
      final Vector3D actual = c.cameraGetRight();
      dumpVector("right expected", expected);
      dumpVector("right actual", actual);
      Assert.assertEquals(expected.x(), actual.x(), 0.00001);
      Assert.assertEquals(expected.y(), actual.y(), 0.00001);
      Assert.assertEquals(expected.z(), actual.z(), 0.00001);
    }

    {
      final Vector3D expected = Vector3D.of(0.0, 1.0, 0.0);
      final Vector3D actual = c.cameraGetUp();
      dumpVector("up expected", expected);
      dumpVector("up actual", actual);
      Assert.assertEquals(expected.x(), actual.x(), 0.00001);
      Assert.assertEquals(expected.y(), actual.y(), 0.00001);
      Assert.assertEquals(expected.z(), actual.z(), 0.00001);
    }

    this.compareSnapshot(c);
  }

  @Test
  public void testDirectionsTurnUp45()
  {
    final JCameraFPSStyleType c = JCameraFPSStyle.newCamera();

    System.out.println("-- up 45");

    c.cameraRotateAroundHorizontal(Math.toRadians(45.0));

    {
      final Vector3D expected = Vector3D.of(0.0, 0.707107, -0.707107);
      final Vector3D actual = c.cameraGetForward();
      dumpVector("forward expected", expected);
      dumpVector("forward actual", actual);
      Assert.assertEquals(expected.x(), actual.x(), 0.00001);
      Assert.assertEquals(expected.y(), actual.y(), 0.00001);
      Assert.assertEquals(expected.z(), actual.z(), 0.00001);
    }

    {
      final Vector3D expected = Vector3D.of(1.0, 0.0, 0.0);
      final Vector3D actual = c.cameraGetRight();
      dumpVector("right expected", expected);
      dumpVector("right actual", actual);
      Assert.assertEquals(expected.x(), actual.x(), 0.00001);
      Assert.assertEquals(expected.y(), actual.y(), 0.00001);
      Assert.assertEquals(expected.z(), actual.z(), 0.00001);
    }

    {
      final Vector3D expected = Vector3D.of(0.0, 0.707107, 0.707107);
      final Vector3D actual = c.cameraGetUp();
      dumpVector("up expected", expected);
      dumpVector("up actual", actual);
      Assert.assertEquals(expected.x(), actual.x(), 0.00001);
      Assert.assertEquals(expected.y(), actual.y(), 0.00001);
      Assert.assertEquals(expected.z(), actual.z(), 0.00001);
    }

    this.compareSnapshot(c);
  }

  @Test
  public void testDirectionsTurnUp90()
  {
    final JCameraFPSStyleType c = JCameraFPSStyle.newCamera();

    System.out.println("-- up 90");

    c.cameraClampHorizontalDisable();
    c.cameraRotateAroundHorizontal(Math.toRadians(89.999));

    {
      final Vector3D expected = Vector3D.of(0.0, 1.0, -0.00001);
      final Vector3D actual = c.cameraGetForward();
      dumpVector("forward expected", expected);
      dumpVector("forward actual", actual);
      Assert.assertEquals(expected.x(), actual.x(), 0.00001);
      Assert.assertEquals(expected.y(), actual.y(), 0.00001);
      Assert.assertEquals(expected.z(), actual.z(), 0.00001);
    }

    {
      final Vector3D expected = Vector3D.of(1.0, 0.0, 0.0);
      final Vector3D actual = c.cameraGetRight();
      dumpVector("right expected", expected);
      dumpVector("right actual", actual);
      Assert.assertEquals(expected.x(), actual.x(), 0.00001);
      Assert.assertEquals(expected.y(), actual.y(), 0.00001);
      Assert.assertEquals(expected.z(), actual.z(), 0.00001);
    }

    {
      final Vector3D expected = Vector3D.of(0.0, 0.00001, 1.0);
      final Vector3D actual = c.cameraGetUp();
      dumpVector("up expected", expected);
      dumpVector("up actual", actual);
      Assert.assertEquals(expected.x(), actual.x(), 0.00001);
      Assert.assertEquals(expected.y(), actual.y(), 0.00001);
      Assert.assertEquals(expected.z(), actual.z(), 0.00001);
    }

    this.compareSnapshot(c);
  }

  @Test
  public void testDirectionsVertical()
  {
    final JCameraFPSStyleType c = JCameraFPSStyle.newCamera();

    for (double index = 0.0; index < 360.0; index += 0.1) {
      c.cameraRotateAroundVertical(0.1);
      this.compareSnapshot(c);
    }
  }

  @Test
  public void testEquality()
  {
    final JCameraFPSStyleType c0 = JCameraFPSStyle.newCamera();

    c0.cameraRotateAroundVertical(random() * 100.0);
    c0.cameraRotateAroundHorizontal(random() * 100.0);
    c0.cameraMoveRight(random() * 100.0);
    c0.cameraMoveUp(random() * 100.0);
    c0.cameraMoveForward(random() * 100.0);

    final JCameraFPSStyleType c1 = JCameraFPSStyle.newCameraFrom(c0);

    System.out.println("c0: " + c0);
    System.out.println("c1: " + c1);

    Assert.assertEquals(c0, c1);
    Assert.assertNotEquals(c0, null);
    Assert.assertNotEquals(c0, Integer.valueOf(23));

    Assert.assertEquals(c0.hashCode(), c1.hashCode());
    Assert.assertEquals(c0.toString(), c1.toString());

    this.compareSnapshot(c1);
    this.compareSnapshot(c0);
  }

  @Test
  public void testMoveForwardInitial()
  {
    final JCameraFPSStyleType c = JCameraFPSStyle.newCamera();

    System.out.println("-- move forward initial");

    c.cameraMoveForward(10.0);

    {
      final Vector3D expected = Vector3D.of(0.0, 0.0, -10.0);
      final Vector3D actual = c.cameraGetPosition();
      dumpVector("move forward expected", expected);
      dumpVector("move forward actual", actual);
      Assert.assertEquals(expected.x(), actual.x(), 0.00001);
      Assert.assertEquals(expected.y(), actual.y(), 0.00001);
      Assert.assertEquals(expected.z(), actual.z(), 0.00001);
    }

    this.compareSnapshot(c);
  }

  @Test
  public void testSnapshotEquality()
  {
    final JCameraFPSStyleType c0 = JCameraFPSStyle.newCamera();

    c0.cameraRotateAroundVertical(random() * 100.0);
    c0.cameraRotateAroundHorizontal(random() * 100.0);
    c0.cameraMoveRight(random() * 100.0);
    c0.cameraMoveUp(random() * 100.0);
    c0.cameraMoveForward(random() * 100.0);

    final JCameraFPSStyleType c1 = JCameraFPSStyle.newCameraFrom(c0);

    c1.cameraRotateAroundVertical(random() * 100.0);
    c1.cameraRotateAroundHorizontal(random() * 100.0);
    c1.cameraMoveRight(random() * 100.0);
    c1.cameraMoveUp(random() * 100.0);
    c1.cameraMoveForward(random() * 100.0);

    System.out.println("c0: " + c0);
    System.out.println("c1: " + c1);

    final JCameraFPSStyleSnapshot snap_0 = JCameraFPSStyleSnapshots.of(c0);
    final JCameraFPSStyleSnapshot snap_1 = JCameraFPSStyleSnapshots.of(c1);

    Assert.assertEquals(snap_0, snap_0);
    Assert.assertNotEquals(snap_0, snap_1);
    Assert.assertNotEquals(snap_0, null);
    Assert.assertNotEquals(snap_0, Integer.valueOf(23));

    Assert.assertEquals(snap_0.hashCode(), snap_0.hashCode());
    Assert.assertNotEquals(snap_0.hashCode(), snap_1.hashCode());

    Assert.assertEquals(snap_0.toString(), snap_0.toString());
    Assert.assertNotEquals(snap_0.toString(), snap_1.toString());
  }

  @Test
  public void testSnapshotInterpolation()
  {
    final JCameraFPSStyleType c0 = JCameraFPSStyle.newCamera();

    c0.cameraRotateAroundVertical(random() * 100.0);
    c0.cameraRotateAroundHorizontal(random() * 100.0);
    c0.cameraMoveRight(random() * 100.0);
    c0.cameraMoveUp(random() * 100.0);
    c0.cameraMoveForward(random() * 100.0);

    final JCameraFPSStyleType c1 = JCameraFPSStyle.newCamera();

    System.out.println("c0: " + c0);
    System.out.println("c1: " + c1);

    final JCameraFPSStyleSnapshot snap_0 = JCameraFPSStyleSnapshots.of(c0);
    final JCameraFPSStyleSnapshot snap_1 = JCameraFPSStyleSnapshots.of(c1);

    final JCameraFPSStyleSnapshot snap_interp0 =
      JCameraFPSStyleSnapshots.interpolate(snap_0, snap_1, 0.0);
    final JCameraFPSStyleSnapshot snap_interp1 =
      JCameraFPSStyleSnapshots.interpolate(snap_0, snap_1, 1.0);

    final ContextRelative c = new ContextRelative();
    {
      Assert.assertTrue(almostEqual(
        c,
        snap_0.cameraGetPosition().x(),
        snap_interp0.cameraGetPosition().x()));
      Assert.assertTrue(almostEqual(
        c,
        snap_0.cameraGetPosition().y(),
        snap_interp0.cameraGetPosition().y()));
      Assert.assertTrue(almostEqual(
        c,
        snap_0.cameraGetPosition().z(),
        snap_interp0.cameraGetPosition().z()));

      Assert.assertTrue(almostEqual(
        c, snap_0.cameraGetForward().x(), snap_interp0.cameraGetForward().x()));
      Assert.assertTrue(almostEqual(
        c, snap_0.cameraGetForward().y(), snap_interp0.cameraGetForward().y()));
      Assert.assertTrue(almostEqual(
        c, snap_0.cameraGetForward().z(), snap_interp0.cameraGetForward().z()));

      Assert.assertTrue(almostEqual(
        c, snap_0.cameraGetRight().x(), snap_interp0.cameraGetRight().x()));
      Assert.assertTrue(almostEqual(
        c, snap_0.cameraGetRight().y(), snap_interp0.cameraGetRight().y()));
      Assert.assertTrue(almostEqual(
        c, snap_0.cameraGetRight().z(), snap_interp0.cameraGetRight().z()));

      Assert.assertTrue(almostEqual(
        c, snap_0.cameraGetUp().x(), snap_interp0.cameraGetUp().x()));
      Assert.assertTrue(almostEqual(
        c, snap_0.cameraGetUp().y(), snap_interp0.cameraGetUp().y()));
      Assert.assertTrue(almostEqual(
        c, snap_0.cameraGetUp().z(), snap_interp0.cameraGetUp().z()));

      Assert.assertTrue(almostEqual(
        c,
        snap_0.cameraGetAngleAroundHorizontal(),
        snap_interp0.cameraGetAngleAroundHorizontal()));
      Assert.assertTrue(almostEqual(
        c,
        snap_0.cameraGetAngleAroundVertical(),
        snap_interp0.cameraGetAngleAroundVertical()));
    }

    {
      Assert.assertTrue(almostEqual(
        c,
        snap_1.cameraGetPosition().x(),
        snap_interp1.cameraGetPosition().x()));
      Assert.assertTrue(almostEqual(
        c,
        snap_1.cameraGetPosition().y(),
        snap_interp1.cameraGetPosition().y()));
      Assert.assertTrue(almostEqual(
        c,
        snap_1.cameraGetPosition().z(),
        snap_interp1.cameraGetPosition().z()));

      Assert.assertTrue(almostEqual(
        c, snap_1.cameraGetForward().x(), snap_interp1.cameraGetForward().x()));
      Assert.assertTrue(almostEqual(
        c, snap_1.cameraGetForward().y(), snap_interp1.cameraGetForward().y()));
      Assert.assertTrue(almostEqual(
        c, snap_1.cameraGetForward().z(), snap_interp1.cameraGetForward().z()));

      Assert.assertTrue(almostEqual(
        c, snap_1.cameraGetRight().x(), snap_interp1.cameraGetRight().x()));
      Assert.assertTrue(almostEqual(
        c, snap_1.cameraGetRight().y(), snap_interp1.cameraGetRight().y()));
      Assert.assertTrue(almostEqual(
        c, snap_1.cameraGetRight().z(), snap_interp1.cameraGetRight().z()));

      Assert.assertTrue(almostEqual(
        c, snap_1.cameraGetUp().x(), snap_interp1.cameraGetUp().x()));
      Assert.assertTrue(almostEqual(
        c, snap_1.cameraGetUp().y(), snap_interp1.cameraGetUp().y()));
      Assert.assertTrue(almostEqual(
        c, snap_1.cameraGetUp().z(), snap_interp1.cameraGetUp().z()));

      Assert.assertTrue(almostEqual(
        c,
        snap_1.cameraGetAngleAroundHorizontal(),
        snap_interp1.cameraGetAngleAroundHorizontal()));
      Assert.assertTrue(almostEqual(
        c,
        snap_1.cameraGetAngleAroundVertical(),
        snap_interp1.cameraGetAngleAroundVertical()));
    }
  }

  private static double random()
  {
    return Math.random();
  }

  @Test
  public void testStrafeInitial()
  {
    final JCameraFPSStyleType c = JCameraFPSStyle.newCamera();

    System.out.println("-- strafe initial");

    c.cameraMoveRight(10.0);

    {
      final Vector3D expected = Vector3D.of(10.0, 0.0, 0.0);
      final Vector3D actual = c.cameraGetPosition();
      dumpVector("strafe expected", expected);
      dumpVector("strafe actual", actual);
      Assert.assertEquals(expected.x(), actual.x(), 0.00001);
      Assert.assertEquals(expected.y(), actual.y(), 0.00001);
      Assert.assertEquals(expected.z(), actual.z(), 0.00001);
    }

    this.compareSnapshot(c);
  }

  @Test
  public void testStrafeLeft90_0()
  {
    final JCameraFPSStyleType c = JCameraFPSStyle.newCamera();

    System.out.println("-- strafe left 90");

    c.cameraRotateAroundVertical(Math.toRadians(90));
    c.cameraMoveRight(10.0);

    {
      final Vector3D expected = Vector3D.of(0.0, 0.0, -10.0);
      final Vector3D actual = c.cameraGetPosition();
      dumpVector("strafe expected", expected);
      dumpVector("strafe actual", actual);
      Assert.assertEquals(expected.x(), actual.x(), 0.00001);
      Assert.assertEquals(expected.y(), actual.y(), 0.00001);
      Assert.assertEquals(expected.z(), actual.z(), 0.00001);
    }

    this.compareSnapshot(c);
  }

  @Test
  public void testStrafeLeft90_1()
  {
    final JCameraFPSStyleType c = JCameraFPSStyle.newCamera();

    System.out.println("-- strafe left 90");

    c.cameraRotateAroundVertical(Math.toRadians(90));
    c.cameraMoveRight(-10.0);

    {
      final Vector3D expected = Vector3D.of(0.0, 0.0, 10.0);
      final Vector3D actual = c.cameraGetPosition();
      dumpVector("strafe expected", expected);
      dumpVector("strafe actual", actual);
      Assert.assertEquals(expected.x(), actual.x(), 0.00001);
      Assert.assertEquals(expected.y(), actual.y(), 0.00001);
      Assert.assertEquals(expected.z(), actual.z(), 0.00001);
    }

    this.compareSnapshot(c);
  }

  @Test
  public void testStrafeLeft90_2()
  {
    final JCameraFPSStyleType c = JCameraFPSStyle.newCamera();

    System.out.println("-- strafe left 90");

    c.cameraRotateAroundVertical(Math.toRadians(90));
    c.cameraSetPosition3(0.0, 0.0, -10.0);
    c.cameraMoveRight(-10.0);

    {
      final Vector3D expected = Vector3D.of(0.0, 0.0, 0.0);
      final Vector3D actual = c.cameraGetPosition();
      dumpVector("strafe expected", expected);
      dumpVector("strafe actual", actual);
      Assert.assertEquals(expected.x(), actual.x(), 0.00001);
      Assert.assertEquals(expected.y(), actual.y(), 0.00001);
      Assert.assertEquals(expected.z(), actual.z(), 0.00001);
    }

    this.compareSnapshot(c);
  }

  @Test
  public void testStrafeLeft90_3()
  {
    final JCameraFPSStyleType c = JCameraFPSStyle.newCamera();

    System.out.println("-- strafe left 90");

    c.cameraRotateAroundVertical(Math.toRadians(90));
    c.cameraSetPosition(Vector3D.of(0.0, 0.0, -10.0));
    c.cameraMoveRight(-10.0);

    {
      final Vector3D expected = Vector3D.of(0.0, 0.0, 0.0);
      final Vector3D actual = c.cameraGetPosition();
      dumpVector("strafe expected", expected);
      dumpVector("strafe actual", actual);
      Assert.assertEquals(expected.x(), actual.x(), 0.00001);
      Assert.assertEquals(expected.y(), actual.y(), 0.00001);
      Assert.assertEquals(expected.z(), actual.z(), 0.00001);
    }

    this.compareSnapshot(c);
  }

  private interface ViewSpace
  {
    // Nothing
  }

  private interface WorldSpace
  {
    // Nothing
  }
}