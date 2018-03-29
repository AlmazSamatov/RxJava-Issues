package almaz.issues.Presenter;

import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import almaz.issues.GithubAPI.APIHelper;
import almaz.issues.ObjectClasses.Comment;
import almaz.issues.ObjectClasses.Issue;
import almaz.issues.View.IssueView;
import almaz.issues.Model.IssueModel;
import almaz.issues.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class IssuePresenter{

    private IssueView view;
    private IssueModel model;

    public IssuePresenter(IssueView view){
        this.view = view;
        model = new IssueModel(view.getContext());
    }

    public void loadAndSetAvatar(ImageView avatar){
        Picasso.get().load(model.getIssue().getUser().getAvatar_url())
                .placeholder(R.drawable.person)
                .error(R.drawable.person)
                .resize(70, 70)
                .centerCrop()
                .into(avatar);
    }

    public void loadCommentsFromInternet(){
        Retrofit query = new Retrofit.Builder().baseUrl(model.getIssue().getComments_url() + "/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        APIHelper apiHelper = query.create(APIHelper.class);
        Call<List<Comment>> call = apiHelper.getComments(model.getIssue().getComments_url());

        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if (response.isSuccessful()){
                    if(response.body() != null)
                        model.setComments(response.body());
                } else if(response.code() == 403){
                    view.showError("Github API request limit exceeded! Try again after 1 hour.");
                } else{
                    view.showError("Failed to get data!");
                }
                doOperationsAfterResponse();
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                doOperationsAfterResponse();
                view.showError("No Internet connection!");
            }
        });
    }

    private void doOperationsAfterResponse(){
        view.stopRefreshLayout();
        view.hideProgressBar();
        view.showComments(model.getComments());
    }

    public void setAndShowIssue(String issueInJson) {
        Gson gson = new Gson();
        model.setIssue(gson.fromJson(issueInJson, Issue.class));
        view.showIssue(model.getIssue());
    }

    public void saveComments() {
        model.saveComments();
    }

    public void setOnRefreshComments(final SwipeRefreshLayout swipeRefreshLayout) {
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_dark,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_red_dark);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadCommentsFromInternet();
            }
        });
    }

    public void deattachView() {
        view = null;
    }

    public void loadComments(boolean isResumed) {
        view.showProgressBar();
        // if we resumed app then load from memory else from Internet
        if(model.isCommentsInMemory() && isResumed) {
            model.loadComments(new IssueModel.LoadCommentsCallback() {
                @Override
                public void onLoad(List<Comment> comments) {
                    model.setComments(comments);
                    view.hideProgressBar();
                    view.showComments(comments);
                }
            });
        } else{
            loadCommentsFromInternet();
        }

    }
}
