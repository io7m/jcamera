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

package com.io7m.jcamera.examples.jogl.lab;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicReference;

import javax.media.opengl.GLEventListener;
import javax.swing.JLabel;

import net.java.dev.designgridlayout.DesignGridLayout;
import net.java.dev.designgridlayout.RowGroup;

import com.io7m.jcamera.JCameraFPSStyleIntegratorType;
import com.io7m.jcamera.JCameraFPSStyleMouseRegion;
import com.io7m.jcamera.JCameraFPSStyleSnapshot;
import com.io7m.jcamera.JCameraFPSStyleType;
import com.io7m.jcamera.JCameraRotationCoefficients;
import com.io7m.jcamera.JCameraScreenOrigin;
import com.io7m.jcamera.examples.jogl.ExampleFPSStyleGLListener;
import com.io7m.jcamera.examples.jogl.ExampleFPSStyleKeyListener;
import com.io7m.jcamera.examples.jogl.ExampleFPSStyleMouseAdapter;
import com.io7m.jcamera.examples.jogl.ExampleFPSStyleSimulationType;
import com.io7m.jcamera.examples.jogl.ExampleRendererType;
import com.io7m.jfunctional.ProcedureType;
import com.io7m.jnull.NullCheck;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseListener;
import com.jogamp.newt.opengl.GLWindow;

@SuppressWarnings({ "boxing" }) final class CameraSimulationFPSStyle implements
  CameraSimulationType
{
  private final JCameraRotationCoefficients                 coefficients;
  private final GLEventListener                             gl_listener;
  private final RowGroup                                    group;
  private final KeyListener                                 key_listener;
  private final ExampleFPSStyleMouseAdapter                 mouse_listener;
  private final AtomicReference<JCameraFPSStyleMouseRegion> mouse_region;
  private final ExampleFPSStyleSimulationType               sim;

  private final CameraFloatSlider                           linear_acceleration;
  private final CameraFloatSlider                           linear_drag;
  private final CameraFloatSlider                           linear_maximum;
  private final CameraVector3Field                          linear_pos;
  private final CameraFloatSlider                           horizontal_drag;
  private final CameraFloatSlider                           horizontal_acceleration;
  private final CameraFloatSlider                           vertical_drag;
  private final CameraFloatSlider                           vertical_acceleration;
  private final CameraFloatSlider                           vertical_maximum;
  private final CameraFloatSlider                           horizontal_maximum;
  private final CameraFloatField                            vertical;
  private final CameraFloatField                            horizontal;

  CameraSimulationFPSStyle(
    final ExampleFPSStyleSimulationType in_sim,
    final ExecutorService in_background_workers,
    final ExampleRendererType in_renderer,
    final GLWindow in_window)
  {
    this.sim = NullCheck.notNull(in_sim);

    this.mouse_region =
      new AtomicReference<JCameraFPSStyleMouseRegion>(
        JCameraFPSStyleMouseRegion.newRegion(
          JCameraScreenOrigin.SCREEN_ORIGIN_BOTTOM_LEFT,
          in_window.getWidth(),
          in_window.getHeight()));

    this.key_listener =
      new ExampleFPSStyleKeyListener(
        in_sim,
        in_background_workers,
        in_renderer,
        in_window);

    final JCameraFPSStyleSnapshot in_snap = in_sim.integrate();

    this.gl_listener =
      new ExampleFPSStyleGLListener(
        in_window,
        in_snap,
        in_sim,
        this.mouse_region,
        in_renderer);

    this.coefficients = new JCameraRotationCoefficients();

    this.mouse_listener =
      new ExampleFPSStyleMouseAdapter(
        this.mouse_region,
        in_sim,
        this.coefficients);

    this.group = new RowGroup();

    final JCameraFPSStyleIntegratorType integrator = this.sim.getIntegrator();
    final float delta = this.sim.getDeltaTime();

    this.vertical = new CameraFloatField();
    this.horizontal = new CameraFloatField();
    this.linear_pos = new CameraVector3Field("Position (w,a,s,d,q,e)");

    this.linear_drag = new CameraFloatSlider("Linear drag", 0.000001f, 1.0f);
    this.linear_drag.setOnChangeListener(new ProcedureType<Float>() {
      @Override public void call(
        final Float x)
      {
        integrator.integratorLinearSetDrag(x);
      }
    });
    this.linear_drag.setCurrent(this.linear_drag.getMinimum());

    this.linear_acceleration =
      new CameraFloatSlider("Linear acceleration", 0.01f, 3.0f);
    this.linear_acceleration.setOnChangeListener(new ProcedureType<Float>() {
      @Override public void call(
        final Float x)
      {
        integrator.integratorLinearSetAcceleration(x / delta);
      }
    });
    this.linear_acceleration
      .setCurrent(this.linear_acceleration.getMaximum());

    this.linear_maximum =
      new CameraFloatSlider("Linear maximum speed", 0.001f, 5.0f);
    this.linear_maximum.setOnChangeListener(new ProcedureType<Float>() {
      @Override public void call(
        final Float x)
      {
        integrator.integratorLinearSetMaximumSpeed(x / delta);
      }
    });
    this.linear_maximum.setCurrent(this.linear_maximum.getMaximum());

    this.horizontal_drag =
      new CameraFloatSlider("Horizontal drag", 0.000001f, 1.0f);
    this.horizontal_drag.setOnChangeListener(new ProcedureType<Float>() {
      @Override public void call(
        final Float x)
      {
        integrator.integratorAngularSetDragHorizontal(x);
      }
    });
    this.horizontal_drag.setCurrent(this.horizontal_drag.getMinimum());

    this.horizontal_acceleration =
      new CameraFloatSlider("Horizontal acceleration", 0.01f, 1.0f);
    this.horizontal_acceleration
      .setOnChangeListener(new ProcedureType<Float>() {
        @Override public void call(
          final Float x)
        {
          integrator.integratorAngularSetAccelerationHorizontal(x / delta);
        }
      });
    this.horizontal_acceleration.setCurrent(this.horizontal_acceleration
      .getMaximum());

    this.horizontal_maximum =
      new CameraFloatSlider("Horizontal maximum", 0.0001f, 0.01f);
    this.horizontal_maximum.setOnChangeListener(new ProcedureType<Float>() {
      @Override public void call(
        final Float x)
      {
        integrator.integratorAngularSetMaximumSpeedHorizontal(x / delta);
      }
    });
    this.horizontal_maximum.setCurrent(this.horizontal_maximum.getMaximum());

    this.vertical_drag =
      new CameraFloatSlider("Vertical drag", 0.000001f, 1.0f);
    this.vertical_drag.setOnChangeListener(new ProcedureType<Float>() {
      @Override public void call(
        final Float x)
      {
        integrator.integratorAngularSetDragVertical(x);
      }
    });
    this.vertical_drag.setCurrent(this.vertical_drag.getMinimum());

    this.vertical_acceleration =
      new CameraFloatSlider("Vertical acceleration", 0.01f, 1.0f);
    this.vertical_acceleration
      .setOnChangeListener(new ProcedureType<Float>() {
        @Override public void call(
          final Float x)
        {
          integrator.integratorAngularSetAccelerationVertical(x / delta);
        }
      });
    this.vertical_acceleration.setCurrent(this.vertical_acceleration
      .getMaximum());

    this.vertical_maximum =
      new CameraFloatSlider("Vertical maximum", 0.0001f, 0.01f);
    this.vertical_maximum.setOnChangeListener(new ProcedureType<Float>() {
      @Override public void call(
        final Float x)
      {
        integrator.integratorAngularSetMaximumSpeedVertical(x / delta);
      }
    });
    this.vertical_maximum.setCurrent(this.vertical_maximum.getMaximum());
  }

  @Override public <A, E extends Exception> A acceptSimulationType(
    final CameraSimulationVisitorType<A, E> v)
    throws E
  {
    return v.fpsStyle(this);
  }

  @Override public boolean cameraIsEnabled()
  {
    return this.sim.cameraIsEnabled();
  }

  @Override public void cameraSetEnabled(
    final boolean b)
  {
    this.sim.cameraSetEnabled(b);
  }

  @Override public void controlsAddToLayout(
    final DesignGridLayout dg)
  {
    dg
      .row()
      .group(this.group)
      .grid(new JLabel("Vertical"))
      .add(this.vertical);
    dg
      .row()
      .group(this.group)
      .grid(new JLabel("Horizontal"))
      .add(this.horizontal);

    this.linear_pos.controlsAddToLayout(dg);
    this.linear_drag.controlsAddToLayout(dg);
    this.linear_acceleration.controlsAddToLayout(dg);
    this.linear_maximum.controlsAddToLayout(dg);
    this.horizontal_drag.controlsAddToLayout(dg);
    this.horizontal_acceleration.controlsAddToLayout(dg);
    this.horizontal_maximum.controlsAddToLayout(dg);
    this.vertical_drag.controlsAddToLayout(dg);
    this.vertical_acceleration.controlsAddToLayout(dg);
    this.vertical_maximum.controlsAddToLayout(dg);
  }

  @Override public void controlsHide()
  {
    this.group.hide();
    this.linear_pos.controlsHide();
    this.linear_drag.controlsHide();
    this.linear_acceleration.controlsHide();
    this.linear_maximum.controlsHide();
    this.horizontal_drag.controlsHide();
    this.horizontal_acceleration.controlsHide();
    this.horizontal_maximum.controlsHide();
    this.vertical_drag.controlsHide();
    this.vertical_acceleration.controlsHide();
    this.vertical_maximum.controlsHide();
  }

  @Override public void controlsShow()
  {
    this.group.forceShow();
    this.linear_pos.controlsShow();
    this.linear_drag.controlsShow();
    this.linear_acceleration.controlsShow();
    this.linear_maximum.controlsShow();
    this.horizontal_drag.controlsShow();
    this.horizontal_acceleration.controlsShow();
    this.horizontal_maximum.controlsShow();
    this.vertical_drag.controlsShow();
    this.vertical_acceleration.controlsShow();
    this.vertical_maximum.controlsShow();
  }

  @Override public GLEventListener getGLEventListener()
  {
    return this.gl_listener;
  }

  @Override public KeyListener getKeyListener()
  {
    return this.key_listener;
  }

  @Override public MouseListener getMouseListener()
  {
    return this.mouse_listener;
  }

  @Override public void updatePeriodic()
  {
    final JCameraFPSStyleType c = this.sim.getCamera();

    this.linear_pos.setValue(c.cameraGetPosition());
    this.horizontal.setValue(c.cameraGetAngleAroundHorizontal());
    this.vertical.setValue(c.cameraGetAngleAroundVertical());
  }
}