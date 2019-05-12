package org.pursuit.ar_wrld;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Trackable;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.PlaneRenderer;
import com.google.ar.sceneform.rendering.Texture;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ArFragment arFragment;
    private ModelLoader modelLoader;
    int numOfModels = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        modelLoader = new ModelLoader(new WeakReference<>(this));

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.sceneform_fragment);

        arFragment.getPlaneDiscoveryController().hide();
        arFragment.getPlaneDiscoveryController().setInstructionView(null);

        arFragment.getArSceneView().getScene().addOnUpdateListener(this::onUpdate);

        arFragment.getArSceneView().getScene().addOnUpdateListener(frameTime -> {
            arFragment.onUpdate(frameTime);

        });
        changetexture();
        initializeGallery();
    }

    private void onUpdate(FrameTime frameTime) {
        if (numOfModels == 1) return;
        Frame frame = arFragment.getArSceneView().getArFrame();
        Collection<Plane> planes = frame.getUpdatedTrackables(Plane.class);
        for (Plane plane : planes) {
            if (plane.getTrackingState() == TrackingState.TRACKING) {
                addObject(Uri.parse("andy.sfb"));
                break;
            }
        }
    }

    private android.graphics.Point getScreenCenter() {
        View vw = findViewById(android.R.id.content);
        return new android.graphics.Point(vw.getWidth() / 2, vw.getHeight() / 2);
    }

    private void initializeGallery() {
        LinearLayout gallery = findViewById(R.id.gallery_layout);

        ImageView andy = new ImageView(this);
        andy.setImageResource(R.drawable.droid_thumb);
        andy.setContentDescription("andy");
        andy.setOnClickListener(view -> {
            addObject(Uri.parse("andy.sfb"));
        });
        gallery.addView(andy);

        ImageView cabin = new ImageView(this);
        cabin.setImageResource(R.drawable.cabin_thumb);
        cabin.setContentDescription("cabin");
        cabin.setOnClickListener(view -> {
            addObject(Uri.parse("Cabin.sfb"));
        });
        gallery.addView(cabin);

        ImageView house = new ImageView(this);
        house.setImageResource(R.drawable.house_thumb);
        house.setContentDescription("house");
        house.setOnClickListener(view -> {
            addObject(Uri.parse("House.sfb"));
        });
        gallery.addView(house);

        ImageView igloo = new ImageView(this);
        igloo.setImageResource(R.drawable.igloo_thumb);
        igloo.setContentDescription("igloo");
        igloo.setOnClickListener(view -> {
            addObject(Uri.parse("igloo.sfb"));
        });
        gallery.addView(igloo);
    }

    private void addObject(Uri model) {
        numOfModels++;
        Frame frame = arFragment.getArSceneView().getArFrame();
        android.graphics.Point pt = getScreenCenter();
        List<HitResult> hits;
        if (frame != null) {
            hits = frame.hitTest(pt.x, pt.y);
            for (HitResult hit : hits) {
                Trackable trackable = hit.getTrackable();
                if (trackable instanceof Plane &&
                        ((Plane) trackable).isPoseInPolygon(hit.getHitPose())) {
                    modelLoader.loadModel(hit.createAnchor(), model);
                    break;

                }
            }
        }
    }

    //TODO add logic to track lives to track per model, not overall
    public void addNodeToScene(Anchor anchor, ModelRenderable renderable) {
        AnchorNode anchorNode = new AnchorNode(anchor);
        TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
        node.setRenderable(renderable);
        node.setParent(anchorNode);
        node.setLocalPosition(new Vector3(0f, 2f, 0f));
        modelLoader.setNumofLivesModel0(2);
        arFragment.getArSceneView().getScene().addChild(anchorNode);

        node.setOnTapListener((hitTestResult, motionEvent) -> {
            if (0 < modelLoader.getNumofLivesModel0()) {
                modelLoader.setNumofLivesModel0(modelLoader.getNumofLivesModel0() - 1);
            } else {
                anchorNode.removeChild(node);
            }
            Toast.makeText(MainActivity.this, "MODEL HAS 0 " + modelLoader.getNumofLivesModel0() + " LIVES LEFT!", Toast.LENGTH_SHORT).show();
        });
        node.select();

        TransformableNode node1 = new TransformableNode(arFragment.getTransformationSystem());
        node1.setRenderable(renderable);
        node1.setParent(anchorNode);
        node1.setWorldPosition(new Vector3(0f, 0f, -2f));
        modelLoader.setNumofLivesModel1(2);
        node1.setOnTapListener((hitTestResult, motionEvent) -> {
            if (0 < modelLoader.getNumofLivesModel1()) {
                modelLoader.setNumofLivesModel1(modelLoader.getNumofLivesModel1() - 1);
            } else {
                anchorNode.removeChild(node1);
            }
            Toast.makeText(MainActivity.this, "MODEL HAS 1 " + modelLoader.getNumofLivesModel1() + " LIVES LEFT!", Toast.LENGTH_SHORT).show();
        });
        node1.select();

        TransformableNode node2 = new TransformableNode(arFragment.getTransformationSystem());
        node2.setRenderable(renderable);
        node2.setParent(anchorNode);
        node2.setWorldPosition(new Vector3(1f, 0f, 0f));
        modelLoader.setNumofLivesModel2(2);
        node2.setOnTapListener((hitTestResult, motionEvent) -> {
            if (0 < modelLoader.getNumofLivesModel2()) {
                modelLoader.setNumofLivesModel2(modelLoader.getNumofLivesModel2() - 1);
            } else {
                anchorNode.removeChild(node2);
            }
            Toast.makeText(MainActivity.this, "MODEL HAS 2 " + modelLoader.getNumofLivesModel2() + " LIVES LEFT!", Toast.LENGTH_SHORT).show();
        });
        node2.select();
    }

    public void onException(Throwable throwable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(throwable.getMessage())
                .setTitle("Codelab error!");
        AlertDialog dialog = builder.create();
        dialog.show();
        return;
    }

    public void changetexture() {
        Texture.Sampler sampler =
                Texture.Sampler.builder()
                        .setMinFilter(Texture.Sampler.MinFilter.LINEAR)
                        .setWrapMode(Texture.Sampler.WrapMode.REPEAT)
                        .build();
        Texture.builder()
                .setSource(this, R.drawable.testing_carpet_texture)
                .setSampler(sampler)
                .build()
                .thenAccept(texture -> {
                    arFragment.getArSceneView().getPlaneRenderer()
                            .getMaterial().thenAccept(material ->
                            material.setTexture(PlaneRenderer.MATERIAL_TEXTURE, texture));
                });
    }

}
