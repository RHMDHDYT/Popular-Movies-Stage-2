package com.rahmad.popularmoviesstage2

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.rahmad.popularmoviesstage2.db.FavoriteContract.FavoriteEntry
import com.rahmad.popularmoviesstage2.models.movielist.MovieResponse
import com.rahmad.popularmoviesstage2.models.movielist.MovieResultsItem
import com.rahmad.popularmoviesstage2.util.ApiClient
import com.rahmad.popularmoviesstage2.util.ApiInterface
import com.rahmad.popularmoviesstage2.util.AppSharedPref
import com.rahmad.popularmoviesstage2.util.NetworkUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * The type Main activity.
 */
class MainActivity : AppCompatActivity() {
    private var textInfoCaption: TextView? = null
    private var progressBar: ProgressBar? = null
    private var callMovies: Call<MovieResponse>? = null
    private val moviesList: MutableList<MovieResultsItem> = mutableListOf()
    private var moviesAdapter: MoviesAdapter? = null
    private var swipeContainer: SwipeRefreshLayout? = null
    private var currentState: Int? = null
    private var appSharedPref: AppSharedPref? = null
    private var apiService: ApiInterface? = null
    private var savedInstanceState: Bundle? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.savedInstanceState = savedInstanceState
        textInfoCaption = findViewById<View>(R.id.text_caption) as TextView
        progressBar = findViewById<View>(R.id.progress_bar) as ProgressBar
        val listMovies = findViewById<View>(R.id.grid_poster_movies) as RecyclerView
        swipeContainer = findViewById<View>(R.id.swipeContainer) as SwipeRefreshLayout
        appSharedPref = AppSharedPref(this)
        //get default state
        currentState = appSharedPref!!.sortingState
        //set toolbar title
        setToolbarTitle()
        moviesAdapter = MoviesAdapter(
            moviesList,
            applicationContext,
            object : MoviesAdapter.MoviesAdapterOnClickHandler {
                override fun onClick(data: String?) {
                    val intent = Intent(applicationContext, DetailActivity::class.java)
                    intent.putExtra(ConstantData.MOVIE_ID_KEY, data)
                    startActivity(intent)
                }
            })
        //set list config
        listMovies.setHasFixedSize(true)
        listMovies.layoutManager = GridLayoutManager(this, COLUMN_SIZE)
        listMovies.adapter = moviesAdapter

        //set api caller
        apiService = ApiClient.getClient().create(ApiInterface::class.java)

        //get movies data
        clearListData()
        getMoviesData(savedInstanceState)

        //set on refresh listener
        swipeContainer!!.setOnRefreshListener { getMoviesData(null, false) }
    }

    override fun onResume() {
        super.onResume()

        //reload list when current list is favorited movies when activity resumed to refresh active favorited movies
        if (currentState == favoriteState) {
            clearListData()
            getMoviesData(savedInstanceState)
        }
    }

    private fun setToolbarTitle() {
        if (currentState == popularState) {
            title = getString(R.string.most_popular_movies_caption)
        } else if (currentState == highestRatedState) {
            title = getString(R.string.highest_rated_movies_caption)
        } else if (currentState == favoriteState) {
            title = getString(R.string.favorite_movies_caption)
        }
    }

    private fun getMoviesData(savedInstanceState: Bundle?) {
        getMoviesData(savedInstanceState, true)
    }

    private fun getMoviesData(savedInstanceState: Bundle?, enableLoadingIndicatorCenter: Boolean) {

        //if there is connection then continue
        if (NetworkUtil.isOnline(this)) {
            hideTextCaption()

            //null and check saved instance state
            if (savedInstanceState == null || !savedInstanceState.containsKey(
                    KEY_SAVED_INSTANCE_STATE
                )
            ) {
                if (currentState == popularState) {
                    getPopularMovies(enableLoadingIndicatorCenter)
                } else if (currentState == highestRatedState) {
                    getTopRatedMovies(enableLoadingIndicatorCenter)
                } else if (currentState == favoriteState) {
                    getFavoritedMovies(enableLoadingIndicatorCenter)
                }
            } else {
                //if instance not null and contain key bundle then add the data to list
                val parcelableData: MutableList<MovieResultsItem> =
                    savedInstanceState.getParcelableArrayList(
                        KEY_SAVED_INSTANCE_STATE
                    )!!
                moviesList.addAll(parcelableData)

                //if movielist null or size = 0 then show no data caption and clear list
                if (moviesList.size == 0) {
                    showNoDataCaption()
                    clearListData()
                } else {
                    //else of that refresh the adapter
                    hideTextCaption()
                    moviesAdapter!!.notifyDataSetChanged()
                }
            }
        } else {
            //if didn't have connection then show the UI configuration to show the error
            showNoConnectivityCaption()
            clearListData()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        //put list and cast to arraylist
        outState.putParcelableArrayList(
            KEY_SAVED_INSTANCE_STATE,
            moviesList as ArrayList<out Parcelable?>
        )
        //save current flag to shared preferences
        appSharedPref!!.sortingState = currentState
        appSharedPref!!.commit()
        super.onSaveInstanceState(outState)
    }

    private fun getTopRatedMovies(withLoading: Boolean) {
        hideTextCaption()
        if (withLoading) {
            showProgressBar()
        }

        //call the api
        callMovies = apiService!!.getMovies(ConstantData.SORT_BY_TOP_RATED, BuildConfig.API_KEY)
        callMovies?.clone()?.enqueue(object : Callback<MovieResponse?> {
            override fun onResponse(
                call: Call<MovieResponse?>,
                response: Response<MovieResponse?>
            ) {
                processSuccessData(response)
            }

            override fun onFailure(call: Call<MovieResponse?>, t: Throwable) {
                processFailedData()
            }
        })
    }

    private fun getPopularMovies(withLoading: Boolean) {
        hideTextCaption()
        if (withLoading) {
            showProgressBar()
        }

        //call the api
        callMovies = apiService!!.getMovies(ConstantData.SORT_BY_POPULAR, BuildConfig.API_KEY)
        callMovies?.clone()?.enqueue(object : Callback<MovieResponse?> {
            override fun onResponse(
                call: Call<MovieResponse?>,
                response: Response<MovieResponse?>
            ) {
                processSuccessData(response)
            }

            override fun onFailure(call: Call<MovieResponse?>, t: Throwable) {
                processFailedData()
            }
        })
    }

    @SuppressLint("Range")
    private fun getFavoritedMovies(withLoading: Boolean) {
        hideTextCaption()
        if (withLoading) {
            showProgressBar()
        }
        val cursor = contentResolver
            .query(FavoriteEntry.CONTENT_URI, null, null, null, null)
        val listFavoritedMovies: MutableList<MovieResultsItem> = mutableListOf()
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val `object` = MovieResultsItem(
                        id = cursor.getInt(cursor.getColumnIndex(FavoriteEntry.COLUMN_MOVIE_ID)),
                        originalTitle = cursor.getString(cursor.getColumnIndex(FavoriteEntry.COLUMN_TITLE)),
                        posterPath = cursor.getString(cursor.getColumnIndex(FavoriteEntry.COLUMN_THUMBNAIL))
                    )
                    listFavoritedMovies.add(`object`)
                } while (cursor.moveToNext())
                processOfflineData(listFavoritedMovies)
            } else {
                showNoFavoritedData()
            }
            cursor.close()
        } else {
            showNoFavoritedData()
        }
    }

    private fun processSuccessData(response: Response<MovieResponse?>) {
        swipeContainer!!.isRefreshing = false
        hideProgressBar()

        //check null and empty data
        if (response.body() == null || response.body()!!.results.isEmpty()) {
            showNoDataCaption()
        } else {
            hideTextCaption()
            moviesList.clear()
            moviesList.addAll(response.body()!!.results)
        }
        moviesAdapter!!.notifyDataSetChanged()
    }

    private fun processOfflineData(listFavoritedMovies: List<MovieResultsItem>) {
        swipeContainer!!.isRefreshing = false
        hideProgressBar()

        //check null and empty data
        if (listFavoritedMovies.isEmpty()) {
            showNoDataCaption()
        } else {
            hideTextCaption()
            moviesList.clear()
            moviesList.addAll(listFavoritedMovies)
        }
        moviesAdapter!!.notifyDataSetChanged()
    }

    private fun processFailedData() {
        //show UI error state
        swipeContainer!!.isRefreshing = false
        hideProgressBar()
        showFailedCaption()
    }

    private fun showNoFavoritedData() {
        //show UI error state
        swipeContainer!!.isRefreshing = false
        hideProgressBar()
        showNoFavoritedCaption()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        //options for switching to highest rated movies
        if (id == R.id.action_highest_rated) {
            title = getString(R.string.highest_rated_movies_caption)
            currentState = highestRatedState
            appSharedPref!!.sortingState = highestRatedState
            appSharedPref!!.commit()
            clearListData()
            getMoviesData(null)
            return true
            //options for switching to popular movies
        } else if (id == R.id.action_most_popular) {
            currentState = popularState
            appSharedPref!!.sortingState = popularState
            appSharedPref!!.commit()
            title = getString(R.string.most_popular_movies_caption)
            clearListData()
            getMoviesData(null)
            return true
        } else if (id == R.id.action_favorited) {
            currentState = favoriteState
            appSharedPref!!.sortingState = favoriteState
            appSharedPref!!.commit()
            title = getString(R.string.favorite_movies_caption)
            clearListData()
            getMoviesData(null)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun clearListData() {
        moviesList.clear()
        moviesAdapter!!.notifyDataSetChanged()
    }

    private fun showProgressBar() {
        progressBar!!.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar!!.visibility = View.GONE
    }

    private fun showNoConnectivityCaption() {
        textInfoCaption!!.text = getString(R.string.no_connectivity_caption)
        textInfoCaption!!.visibility = View.VISIBLE
    }

    private fun showNoDataCaption() {
        textInfoCaption!!.text = getString(R.string.no_data_caption)
        textInfoCaption!!.visibility = View.VISIBLE
    }

    private fun showFailedCaption() {
        textInfoCaption!!.text = getString(R.string.failed_getting_data_caption)
        textInfoCaption!!.visibility = View.VISIBLE
    }

    private fun showNoFavoritedCaption() {
        textInfoCaption!!.text = getString(R.string.no_favorited_data_caption)
        textInfoCaption!!.visibility = View.VISIBLE
    }

    private fun hideTextCaption() {
        textInfoCaption!!.visibility = View.GONE
    }

    companion object {
        private const val COLUMN_SIZE = 2
        private const val KEY_SAVED_INSTANCE_STATE = "movie_poster_key"
        private const val popularState = 1
        private const val highestRatedState = 2
        private const val favoriteState = 3
        private const val TIME_DELAY = 2000
        private const val back_pressed: Long = 0
    }
}