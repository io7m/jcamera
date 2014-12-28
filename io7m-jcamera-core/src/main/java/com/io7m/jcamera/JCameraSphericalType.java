/*
 * Copyright © 2014 <code@io7m.com> http://io7m.com
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

package com.io7m.jcamera;

import com.io7m.jtensors.VectorReadable3FType;

/**
 * <p>
 * The type of <i>spherical</i> style flying cameras that point towards a
 * given point <code>p</code> and allow for:
 * </p>
 * <ul>
 * <li>Orbiting horizontally around <code>p</code></li>
 * <li>Orbiting vertically around <code>p</code></li>
 * </ul>
 */

public interface JCameraSphericalType extends
  JCameraType,
  JCameraSphericalReadableType
{
  /**
   * Do not clamp rotations around the horizontal axis.
   *
   * @see #cameraClampInclineEnable(float, float)
   */

  void cameraClampInclineDisable();

  /**
   * Clamp the possible rotations around the horizontal axis to the given
   * bounds.
   *
   * @param min
   *          The minimum angle
   * @param max
   *          The maximum angle
   */

  void cameraClampInclineEnable(
    final float min,
    final float max);

  /**
   * Do not clamp the length of the radius.
   *
   * @see #cameraClampRadiusEnable(float, float)
   */

  void cameraClampRadiusDisable();

  /**
   * Clamp the length of the radius to the given bounds.
   *
   * @param min
   *          The minimum length
   * @param max
   *          The maximum length
   */

  void cameraClampRadiusEnable(
    final float min,
    final float max);

  /**
   * <p>
   * Move the target point of the camera <i>forward</i> <code>u</code> units
   * iff <code>u</code> is positive, or <i>backward</i> <code>u</code> units
   * iff <code>u</code> is negative.
   * </p>
   * <p>
   * The <i>forward</i> direction, in this case, means the current view
   * direction projected onto the X/Z plane. This means that the camera will
   * not move along the Y axis.
   * </p>
   *
   * @param u
   *          The units to move
   */

  void cameraMoveTargetForwardOnXZ(
    float u);

  /**
   * <p>
   * Move the target point of the camera <i>right</i> <code>u</code> units iff
   * <code>u</code> is positive, or <i>left</i> <code>u</code> units iff
   * <code>u</code> is negative.
   * </p>
   * <p>
   * The <i>right</i> direction, in this case, means the direction
   * perpendicular to the current view direction, <code>-π / 2</code> radians
   * around the global Y axis.
   * </p>
   *
   * @param u
   *          The units to move
   */

  void cameraMoveTargetRight(
    float u);

  /**
   * <p>
   * Move the target point of the camera <i>up</i> <code>u</code> units iff
   * <code>u</code> is positive, or <i>down</i> <code>u</code> units iff
   * <code>u</code> is negative.
   * </p>
   * <p>
   * The <i>up</i> direction, in this case, means the direction towards
   * positive infinity on the global Y axis.
   * </p>
   *
   * @param u
   *          The units to move
   */

  void cameraMoveTargetUp(
    float u);

  /**
   * @param r
   *          The radians to rotate
   */

  void cameraOrbitHeading(
    float r);

  /**
   * @return <code>true</code> if the rotation has been clamped
   * @see #cameraClampInclineEnable(float, float)
   * @param r
   *          The radians to orbit
   */

  boolean cameraOrbitIncline(
    float r);

  /**
   * Set the heading angle to <code>a</code>.
   *
   * @param a
   *          The angle.
   */

  void cameraSetAngleHeading(
    final float a);

  /**
   * Set the incline angle to <code>a</code>.
   *
   * @param a
   *          The angle.
   */

  void cameraSetAngleIncline(
    final float a);

  /**
   * Set the position of the target point of camera.
   *
   * @param v
   *          The position.
   */

  void cameraSetTargetPosition(
    final VectorReadable3FType v);

  /**
   * Set the position of the target point of camera.
   *
   * @param x
   *          The x coordinate.
   * @param y
   *          The y coordinate.
   * @param z
   *          The z coordinate.
   */

  void cameraSetTargetPosition3f(
    final float x,
    final float y,
    final float z);

  /**
   * Set the zoom (or <i>radius</i>) of the camera to <code>r</code>.
   *
   * @param r
   *          The camera radius.
   */

  void cameraSetZoom(
    float r);

  /**
   * Zoom in the camera by reducing the radius of the sphere.
   *
   * @return <code>true</code> if the radius has been clamped
   * @see #cameraClampRadiusEnable(float, float)
   * @param r
   *          The amount by which to reduce the radius
   */

  boolean cameraZoomIn(
    float r);

  /**
   * Zoom out the camera by increasing the radius of the sphere.
   *
   * @return <code>true</code> if the radius has been clamped
   * @see #cameraClampRadiusEnable(float, float)
   * @param r
   *          The amount by which to increase the radius
   */

  boolean cameraZoomOut(
    float r);
}