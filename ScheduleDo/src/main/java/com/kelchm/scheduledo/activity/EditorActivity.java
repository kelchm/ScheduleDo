package com.kelchm.scheduledo.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.kelchm.scheduledo.R;
import com.kelchm.scheduledo.fragments.EditorFragment;
import com.kelchm.scheduledo.helpers.TasksRepository;
import com.kelchm.scheduledo.models.HoldsProject;
import com.kelchm.scheduledo.models.task.Project;

public class EditorActivity extends FragmentActivity implements HoldsProject {

    private Project project;
    private EditorFragment editorFragment = new EditorFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        if (savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction().add(R.id.container, editorFragment).commit();
        }

        getActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle b = getIntent().getExtras();
        int id = b.getInt("id");

        // Grab the selected project from the database
        TasksRepository tasksRepository = new TasksRepository(this);
        project = tasksRepository.getProject(id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action buttons
        switch (item.getItemId())
        {
            case android.R.id.home:
                Toast.makeText(this, R.string.toast_discarded, Toast.LENGTH_LONG).show();
                finish();
                return true;

            case R.id.action_save_project:
                saveProject();
                Toast.makeText(this, R.string.toast_saved, Toast.LENGTH_LONG).show();
                finish();
                return true;

            case R.id.action_delete_project:
                deleteProject();
                Toast.makeText(this, R.string.toast_project_deleted, Toast.LENGTH_LONG).show();
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public Project getProject() {
        return project;
    }

    private void saveProject() {

        // Get updated project details
        editorFragment.getUpdatedProject();

        // Update the database
        TasksRepository repository = new TasksRepository(this);
        repository.update(project);

        Log.i(NewProjectActivity.class.getName(), "Project updated");
    }

    private void deleteProject() {

        // Update the database
        TasksRepository repository = new TasksRepository(this);
        repository.delete(project);

    }
}
