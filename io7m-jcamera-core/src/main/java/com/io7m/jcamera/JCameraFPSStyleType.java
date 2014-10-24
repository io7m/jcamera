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
 * The type of <i>first-person shooter</i> style flying cameras that allow
 * for:
 * </p>
 * <p>
 * <ul>
 * <li>Rotation around a local X axis</li>
 * <li>Rotation around a global Y axis</li>
 * <li>Movement <i>up</i> and <i>down</i> on the global Y axis</li>
 * <li>Movement <i>left</i> and <i>right</i> on the local X axis</li>
 * <li>Movement <i>forward</i> and <i>backward</i> based on the current
 * orientation on the global Y and local X axes</li>
 * </ul>
 * </p>
 */

public interface JCameraFPSStyleType extends JCameraType
{
  /**
   * Do not clamp rotations around the horizontal axis.
   *
   * @see #cameraClampHorizontalEnable(float, float)
   */

  void cameraClampHorizontalDisable();

  /**
   * Clamp the possible rotations around the horizontal axis to the given
   * bounds.
   *
   * @param min
   *          The minimum angle
   * @param max
   *          The maximum angle
   */

  void cameraClampHorizontalEnable(
    final float min,
    final float max);

  /**
   * @return The forward direction for the camera.
   */

  VectorReadable3FType cameraGetForward();

  /**
   * @return The position of the camera.
   */

  VectorReadable3FType cameraGetPosition();

  /**
   * @return The right direction for the camera.
   */

  VectorReadable3FType cameraGetRight();

  /**
   * @return The up direction for the camera.
   */

  VectorReadable3FType cameraGetUp();

  /**
   * <p>
   * Move the camera <i>forward</i> <code>u</code> units iff <code>u</code> is
   * positive, or <i>backward</i> <code>u</code> units iff <code>u</code> is
   * negative.
   * </p>
   * <p>
   * The <i>forward</i> direction, in this case, means the current view
   * direction.
   * </p>
   *
   * @param u
   *          The units to move
   */

  void cameraMoveForward(
    float u);

  /**
   * <p>
   * Move the camera <i>right</i> <code>u</code> units iff <code>u</code> is
   * positive, or <i>left</i> <code>u</code> units iff <code>u</code> is
   * negative.
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

  void cameraMoveRight(
    float u);

  /**
   * <p>
   * Move the camera <i>up</i> <code>u</code> units iff <code>u</code> is
   * positive, or <i>down</i> <code>u</code> units iff <code>u</code> is
   * negative.
   * </p>
   * <p>
   * The <i>up</i> direction, in this case, means the direction towards
   * positive infinity on the global Y axis.
   * </p>
   *
   * @param u
   *          The units to move
   */

  void cameraMoveUp(
    float u);

  /**
   * <p>
   * Rotate by <code>r</code> radians around the local X axis.
   * </p>
   * <p>
   * Note that by convention, when looking towards negative infinity on an
   * axis, a positive rotation value results in a counter-clockwise rotation
   * on the axis.
   * </p>
   * <p>
   * A human analogy would be that a positive value passed to this function
   * causes the camera to look <i>up</i> towards the sky. A negative value
   * causes the camera to look <i>down</i> towards the ground.
   * </p>
   *
   * @return <code>true</code> if the rotation has been clamped
   * @see #cameraClampHorizontalEnable(float, float)
   * @param r
   *          The radians to rotate
   */

  boolean cameraRotateAroundHorizontal(
    float r);

  /**
   * <p>
   * Rotate by <code>r</code> radians around the global Y axis.
   * </p>
   * <p>
   * Note that by convention, when looking towards negative infinity on an
   * axis, a positive rotation value results in a counter-clockwise rotation
   * on the axis.
   * </p>
   * <p>
   * A human analogy would be that a positive value passed to this function
   * causes the camera to turn <i>left</i>, while a negative value causes the
   * camera to look <i>right</i>.
   * </p>
   *
   * @param r
   *          The radians to rotate
   */

  void cameraRotateAroundVertical(
    float r);

  /**
   * Set the position of the camera.
   *
   * @param v
   *          The position.
   */

  void cameraSetPosition(
    final VectorReadable3FType v);

  /**
   * Set the position of the camera.
   *
   * @param x
   *          The x coordinate.
   * @param y
   *          The y coordinate.
   * @param z
   *          The z coordinate.
   */

  void cameraSetPosition3f(
    final float x,
    final float y,
    final float z);

  /**
   * @return The current camera position.
   */

  VectorReadable3FType getPosition();
}
