package codingblocks.com.gsocinfo.adapters;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.DiffCallback;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.commonsware.cwac.anddown.AndDown;

import codingblocks.com.gsocinfo.R;
import codingblocks.com.gsocinfo.data.model.Project;

import static android.view.View.GONE;

/**
 * Created by harshit on 26/08/17.
 */

public class ProjectAdapter extends PagedListAdapter<Project, ProjectAdapter.ProjectHolder> {

    private static final DiffCallback<Project> DIFF_CALLBACK = new DiffCallback<Project>() {
        @Override
        public boolean areItemsTheSame(@NonNull Project oldItem, @NonNull Project newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Project oldItem, @NonNull Project newItem) {
            return areItemsTheSame(oldItem, newItem);
        }
    };
    private Context context;
    private int count = 0; //keeping track of card item created for setting the background

    public ProjectAdapter() {
        super(DIFF_CALLBACK);
    }

    @Override
    public ProjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.item_project, parent, false);
        return new ProjectHolder(v);
    }

    @Override
    public void onBindViewHolder(final ProjectHolder holder, int position) {
        holder.linearLayoutExpanded.setVisibility(GONE);
        Project currProject = getItem(position);
        holder.studentName.setText(currProject.getStudent().getDisplayName());
        holder.projectName.setText(currProject.getTitle());
        holder.orgName.setText(currProject.getOrganization().getName());
        holder.projectMentor.setText("");

        AndDown andDown = new AndDown();
        holder.projectDesc.setText(Html.fromHtml(andDown.markdownToHtml(currProject.get_abstract())));
//        holder.projectDesc.setText(currProject.get_abstract());
        for (int i = 0; i < currProject.getAssigneeDisplayNames().size(); i++) {
            if (i == currProject.getAssigneeDisplayNames().size() - 1)
                holder.projectMentor.append(currProject.getAssigneeDisplayNames().get(i));
            else
                holder.projectMentor.append(currProject.getAssigneeDisplayNames().get(i) + ", ");
        }
    }


    public class ProjectHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView studentName, projectName, projectMentor, orgName, projectDesc, projectTitle;
        LinearLayout linearLayoutExpanded, linearLayout;
        ImageButton urlImage;

        public ProjectHolder(View itemView) {
            super(itemView);
            projectDesc = itemView.findViewById(R.id.projectDesc);
            studentName = itemView.findViewById(R.id.studentName);
            projectName = itemView.findViewById(R.id.projectName);
            projectMentor = itemView.findViewById(R.id.mentorName);
            orgName = itemView.findViewById(R.id.orgName);
            urlImage = itemView.findViewById(R.id.linkImage);
            linearLayoutExpanded = itemView.findViewById(R.id.linearLayoutExpanded);
            linearLayout = itemView.findViewById(R.id.linearLayoutProject);
            cardView = itemView.findViewById(R.id.projectCard);

            switch (count++ % 4) {
                case 0:
                    linearLayout.setBackground(context.getResources().getDrawable(R.drawable.project_bg_one));
                    linearLayoutExpanded.setBackgroundColor(context.getResources().getColor(R.color.project_one));
                    break;
                case 1:
                    linearLayout.setBackground(context.getResources().getDrawable(R.drawable.project_bg_two));
                    linearLayoutExpanded.setBackgroundColor(context.getResources().getColor(R.color.project_two));
                    break;
                case 2:
                    linearLayout.setBackground(context.getResources().getDrawable(R.drawable.project_bg_three));
                    linearLayoutExpanded.setBackgroundColor(context.getResources().getColor(R.color.project_three));
                    break;
                case 3:
                    linearLayout.setBackground(context.getResources().getDrawable(R.drawable.project_bg_four));
                    linearLayoutExpanded.setBackgroundColor(context.getResources().getColor(R.color.project_four));
                    break;
            }
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!linearLayoutExpanded.isShown()) {
                        linearLayoutExpanded.setVisibility(View.VISIBLE);
                    } else {
                        linearLayoutExpanded.setVisibility(GONE);
                    }
                }
            });
            urlImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Project project = getItem(getAdapterPosition());
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    String url = project.getId();
                    intent.setData(Uri.parse(url));
                    context.startActivity(intent);
                }
            });

        }
    }

}
