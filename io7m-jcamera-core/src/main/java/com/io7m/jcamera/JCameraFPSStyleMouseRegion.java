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

import com.io7m.jequality.annotations.EqualityReference;
import com.io7m.jnull.NullCheck;
import com.io7m.jranges.RangeCheck;

/**
 * An immutable rectangular region that maps mouse positions to rotational
 * coefficients.
 */

@EqualityReference public final class JCameraFPSStyleMouseRegion
{
  /**
   * Construct a new mouse region.
   *
   * @param in_origin
   *          The screen origin.
   * @param in_width
   *          The region width.
   * @param in_height
   *          The region height.
   * @return A new mouse region.
   */

  public static JCameraFPSStyleMouseRegion newRegion(
    final JCameraScreenOrigin in_origin,
    final float in_width,
    final float in_height)
  {
    return new JCameraFPSStyleMouseRegion(in_origin, in_width, in_height);
  }

  private final float               center_x;
  private final float               center_y;
  private final float               height;
  private final JCameraScreenOrigin origin;
  private final float               width;

  private JCameraFPSStyleMouseRegion(
    final JCameraScreenOrigin in_origin,
    final float in_width,
    final float in_height)
  {
    this.origin = NullCheck.notNull(in_origin, "Origin");

    this.height =
      (float) RangeCheck.checkGreaterEqualDouble(
        in_height,
        "Height",
        2.0f,
        "Minimum height");
    this.center_y = this.height / 2.0f;

    this.width =
      (float) RangeCheck.checkGreaterEqualDouble(
        in_width,
        "Width",
        2.0f,
        "Minimum width");
    this.center_x = this.width / 2.0f;
  }

  /**
   * @return The X coordinate of the center of the region
   */

  public float getCenterX()
  {
    return this.center_x;
  }

  /**
   * @return The Y coordinate of the center of the region
   */

  public float getCenterY()
  {
    return this.center_y;
  }

  /**
   * Return the region-space coefficients for the screen-space position
   * <code>(x, y)</code>.
   *
   * @param x
   *          The x coordinate
   * @param y
   *          The y coordinate
   * @param out
   *          The output vector
   */

  public void getCoefficients(
    final float x,
    final float y,
    final JCameraRotationCoefficients out)
  {
    NullCheck.notNull(out, "Output");

    final float fx = x;
    final float fy = y;
    final float rot_v = ((fx - this.center_x) / this.width) * 2.0f;
    final float rot_h = ((fy - this.center_y) / this.height) * 2.0f;

    switch (this.origin) {
      case SCREEN_ORIGIN_BOTTOM_LEFT:
      {
        out.setHorizontal(rot_h);
        out.setVertical(-rot_v);
        break;
      }
      case SCREEN_ORIGIN_TOP_LEFT:
      {
        out.setHorizontal(-rot_h);
        out.setVertical(-rot_v);
        break;
      }
    }
  }

  /**
   * @return The height of the region
   */

  public float getHeight()
  {
    return this.height;
  }

  /**
   * @return The current screen origin
   */

  public JCameraScreenOrigin getOrigin()
  {
    return this.origin;
  }

  /**
   * @return The width of the region
   */

  public float getWidth()
  {
    return this.width;
  }
}