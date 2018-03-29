package almaz.issues.Presenter;
import android.support.v4.widget.SwipeRefreshLayout;

import java.util.List;

import almaz.issues.GithubAPI.APIHelper;
import almaz.issues.ObjectClasses.Issue;
import almaz.issues.View.IssuesView;
import almaz.issues.Model.IssuesModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Almaz on 3/19/2018.
 */

public class IssuesPresenter{

    private IssuesView view;
    private IssuesModel model;

    public IssuesPresenter(IssuesView view){
        this.view = view;
        model = new IssuesModel(view.getContext());
    }

    private void loadIssuesFromInternet(){

        Retrofit query = new Retrofit.Builder().baseUrl("https://api.github.com").
                addConverterFactory(GsonConverterFactory.create()).build();
        APIHelper apiHelper = query.create(APIHelper.class);
        Call<List<Issue>> call = apiHelper.getIssues("ReactiveX", "RxJava");

        call.enqueue(new Callback<List<Issue>>() {
            @Override
            public void onResponse(Call<List<Issue>> call, Response<List<Issue>> response) {
                if (response.isSuccessful()){
                    if(response.body() != null)
                        model.setIssues(response.body());
                } else if(model.isDataInMemory()){
                    view.showError("Failed to update data!");
                } else if(response.code() == 403){
                    view.showError("Github API request limit exceeded! Try again after 1 hour.");
                } else{
                    view.showError("Failed to get data!");
                }
                doOperationsAfterResponse();
            }

            @Override
            public void onFailure(Call<List<Issue>> call, Throwable t) {
                doOperationsAfterResponse();
                if(model.isDataInMemory()){
                    view.showError("No Internet connection! Failed to update data!");
                } else{
                    view.showError("No Internet connection!");
                }
            }
        });
    }

    private void doOperationsAfterResponse(){
        view.stopRefreshLayout();
        view.hideProgressBar();
        view.showIssues(model.getIssues());
    }

    public void saveData() {
        model.saveData();
    }

    public void setOnRefreshListener(final SwipeRefreshLayout swipeRefreshLayout) {
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_dark,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_red_dark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadIssuesFromInternet();
            }
        });
    }

    public void loadIssues() {
        view.showProgressBar();
        // if we resumed app then load from memory else from Internet
        if(model.isDataInMemory()){
            model.loadIssues(new IssuesModel.LoadIssuesCallback() {
                @Override
                public void onLoad(List<Issue> issues) {
                    model.setIssues(issues);
                    view.hideProgressBar();
                    view.showIssues(issues);
                }
            });
        } else{
            loadIssuesFromInternet();
        }
    }
    
    public void deattachView() {
        view = null;
    }
}
