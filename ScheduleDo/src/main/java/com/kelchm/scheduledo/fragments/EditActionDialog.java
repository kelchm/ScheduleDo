package com.kelchm.scheduledo.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.kelchm.scheduledo.R;
import com.kelchm.scheduledo.adapters.CompletionAdapter;
import com.kelchm.scheduledo.adapters.DurationAdapter;
import com.kelchm.scheduledo.helpers.TasksRepository;
import com.kelchm.scheduledo.models.task.Action;

import java.util.Arrays;
import java.util.List;

/**
 * Created by kelchm on 11/17/13.
 */
public class EditActionDialog extends DialogFragment {
    private final static int EDIT_INSTANCE = 1;
    private final static int CREATE_INSTANCE = 2;
    private int actionId;
    private int position;
    private long duration;
    private String name;
    private Button cancelButton;
    private Button saveButton;
    private Button deleteButton;
    private EditText actionNameEditText;
    private Spinner durationSpinner;
    private Spinner completionSpinner;
    private int dialogType;
    private List<Long> durations = Arrays.asList(600000L, 900000L, 1200000L, 1800000L, 2700000L, 3600000L, 7200000L);
    private List<Double> completionPercents = Arrays.asList(0.0, 0.25, 0.50, 0.75, 1.0);

    public EditActionDialog() {
        // Empty constructor required for DialogFragment
    }

    // Create a new creation instance
    static EditActionDialog newCreateInstance() {
        EditActionDialog editActionDialog = new EditActionDialog();

        Bundle args = new Bundle();
        args.putInt("dialogType", CREATE_INSTANCE);
        editActionDialog.setArguments(args);

        return editActionDialog;
    }

    // Create a new editing instance
    static EditActionDialog newEditInstance(Action action) {
        EditActionDialog editActionDialog = new EditActionDialog();

        Bundle args = new Bundle();
        args.putInt("actionId", action.getId());
        args.putInt("dialogType", EDIT_INSTANCE);
        editActionDialog.setArguments(args);

        return editActionDialog;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
        {
            actionId = getArguments().getInt("actionId");
            dialogType = getArguments().getInt("dialogType");
        }

        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo_Light_Dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = inflater.inflate(R.layout.dialog_fragment_edit_action, container);

        cancelButton = (Button) view.findViewById(R.id.button_cancel);
        saveButton = (Button) view.findViewById(R.id.button_save);
        deleteButton = (Button) view.findViewById(R.id.button_delete);
        actionNameEditText = (EditText) view.findViewById(R.id.action_name);
        durationSpinner = (Spinner) view.findViewById(R.id.duration_spinner);
        completionSpinner = (Spinner) view.findViewById(R.id.completion_spinner);

        setupDurationSpinner();
        setupCompletionSpinner();


        // Setup cancel button listener
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, getActivity().getIntent());
                getDialog().dismiss();
            }
        });

        if (dialogType == EDIT_INSTANCE)
        {
            final Action action = retrieveAction(actionId);

            deleteButton.setVisibility(View.VISIBLE);
            getDialog().setTitle(R.string.edit_action);

            // Fill existing data
            actionNameEditText.setText(action.getName());
            durationSpinner.setSelection(durations.indexOf(action.getDuration()));
            completionSpinner.setSelection(completionPercents.indexOf(action.getCompletion()));

            // Setup listeners
            saveButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Get values from dialog fields
                    action.setName(actionNameEditText.getText().toString());
                    action.setDuration(durations.get(durationSpinner.getSelectedItemPosition()));
                    action.setCompletion(completionPercents.get(completionSpinner.getSelectedItemPosition()));

                    updateAction(action);
                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent());
                    getDialog().dismiss();
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    removeAction(actionId);
                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent());
                    getDialog().dismiss();
                }
            });
        }
        else if (dialogType == CREATE_INSTANCE)
        {
            // Remove delete button
            deleteButton.setVisibility(View.GONE);

            // Set dialog title
            getDialog().setTitle(R.string.new_action);

            // Setup save button listener
            saveButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    final EditorFragment editorFragment = ((EditorFragment) getTargetFragment());

                    // Get values from dialog fields
                    String actionName = actionNameEditText.getText().toString();
                    long actionDuration = durations.get(durationSpinner.getSelectedItemPosition());
                    double actionCompletion = completionPercents.get(completionSpinner.getSelectedItemPosition());

                    // Pass back data to parent
                    // TODO: fragment to fragment communication is bad, implement communication through parent activity
                    editorFragment.getProject().addAction(actionName, actionDuration, actionCompletion);
                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent());
                    getDialog().dismiss();
                }
            });
        }

        return view;
    }

    private void setupDurationSpinner() {
        DurationAdapter adapter = new DurationAdapter(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, durations);
        durationSpinner.setAdapter(adapter);
    }

    private void setupCompletionSpinner() {
        CompletionAdapter adapter = new CompletionAdapter(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, completionPercents);
        completionSpinner.setAdapter(adapter);
    }

    private Action retrieveAction(int actionId) {
        TasksRepository repository = new TasksRepository(getActivity());
        return repository.getAction(actionId);
    }

    private void removeAction(int actionId) {
        TasksRepository repository = new TasksRepository(getActivity());
        repository.removeAction(actionId);
    }

    private void updateAction(Action action) {
        TasksRepository repository = new TasksRepository(getActivity());
        repository.update(action);
    }

}