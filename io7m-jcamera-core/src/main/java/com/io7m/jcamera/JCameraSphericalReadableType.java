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
 * Readable interface to {@link JCameraSphericalType}.
 */

public interface JCameraSphericalReadableType extends JCameraReadableType
{
  /**
   * @return The angle around the vertical axis.
   */

  float cameraGetAngleHeading();

  /**
   * @return The angle around the horizontal axis.
   */

  float cameraGetAngleIncline();

  /**
   * @return The forward direction for the camera.
   */

  VectorReadable3FType cameraGetForward();

  /**
   * @return The forward direction for the camera, projected onto the X/Z
   *         plane.
   */

  VectorReadable3FType cameraGetForwardProjectedOnXZ();

  /**
   * @return The position of the camera.
   */

  VectorReadable3FType cameraGetPosition();

  /**
   * @return The right direction for the camera.
   */

  VectorReadable3FType cameraGetRight();

  /**
   * @return The position of the camera target.
   */

  VectorReadable3FType cameraGetTargetPosition();

  /**
   * @return The up direction for the camera.
   */

  VectorReadable3FType cameraGetUp();

  /**
   * @return The zoom (or <i>radius</i>) of the camera.
   */

  float cameraGetZoom();

  /**
   * @return A snapshot of the current camera state.
   */

  JCameraSphericalSnapshot cameraMakeSnapshot();
}