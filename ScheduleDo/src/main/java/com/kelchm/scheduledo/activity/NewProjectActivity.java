package com.kelchm.scheduledo.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.kelchm.scheduledo.R;
import com.kelchm.scheduledo.fragments.EditorFragment;
import com.kelchm.scheduledo.helpers.TasksRepository;
import com.kelchm.scheduledo.models.HoldsProject;
import com.kelchm.scheduledo.models.task.Project;

import org.joda.time.DateTime;

public class NewProjectActivity extends FragmentActivity implements HoldsProject {

    private Project project;
    private TasksRepository repository;
    private EditorFragment editorFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null)
        {
            editorFragment = new EditorFragment();
            repository = new TasksRepository(this);
            getSupportFragmentManager().beginTransaction().add(R.id.container, editorFragment).commit();
            addProject();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action buttons
        switch (item.getItemId())
        {
            case android.R.id.home:
                // Remove the project
                removeProject();
                finish();
                return true;

            case R.id.action_create_project:
                editorFragment.getUpdatedProject();
                repository.update(project);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_project, menu);
        return true;
    }

    private void addProject() {
        project = new Project("", DateTime.now());
        repository.create(project);
    }

    private void removeProject() {
        repository.delete(project);
    }

    @Override
    public Project getProject() {
        return project;
    }
}
