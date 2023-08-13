package com.rahmad.popularmoviesstage2

import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.like.LikeButton
import com.like.OnLikeListener
import com.rahmad.popularmoviesstage2.db.FavoriteContract.FavoriteEntry
import com.rahmad.popularmoviesstage2.models.ModelMovieDetail
import com.rahmad.popularmoviesstage2.services.responses.moviedetail.MovieDetail
import com.rahmad.popularmoviesstage2.services.responses.reviews.ReviewResultsItem
import com.rahmad.popularmoviesstage2.services.responses.reviews.Reviews
import com.rahmad.popularmoviesstage2.services.responses.trailer.Trailer
import com.rahmad.popularmoviesstage2.util.ApiClient
import com.rahmad.popularmoviesstage2.util.ApiInterface
import com.rahmad.popularmoviesstage2.util.DateFormatter
import com.rahmad.popularmoviesstage2.util.NetworkUtil
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by rahmad on 7/7/17.
 * inbox.rahmad@gmail.com
 * Copyright 2017
 */
class DetailActivity : AppCompatActivity(), ReviewsAdapter.MoviesAdapterOnClickHandler {
    private var textTitle: TextView? = null
    private var textSynopsisCaption: TextView? = null
    private var textSynopsisContent: TextView? = null
    private var imageBackDrop: ImageView? = null
    private var imagePoster: ImageView? = null
    private var textRatingContent: TextView? = null
    private var textDateRelease: TextView? = null
    private var ratingBar: RatingBar? = null
    private var recyclerViewReviews: RecyclerView? = null
    private var reviewsAdapter: ReviewsAdapter? = null
    private val reviewResultsItems: MutableList<ReviewResultsItem> = mutableListOf()
    private var collapsingToolbarLayout: CollapsingToolbarLayout? = null
    private var modelMovieDetail: ModelMovieDetail = ModelMovieDetail()
    private var progressDialog: ProgressDialog? = null
    private var builder: AlertDialog.Builder? = null
    private var textInfoCaption: TextView? = null
    private var progressBar: ProgressBar? = null
    private var imagePlayIcon: ImageView? = null
    private var idTrailer: String? = null
    private var buttonFavorite: LikeButton? = null
    private var movieId: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        textTitle = findViewById<View>(R.id.textMovieTitle) as TextView
        textSynopsisCaption = findViewById<View>(R.id.textSynopsisCaption) as TextView
        textSynopsisContent = findViewById<View>(R.id.textSynopsisContent) as TextView
        imageBackDrop = findViewById<View>(R.id.imageViewBackDrop) as ImageView
        imagePoster = findViewById<View>(R.id.imageViewPoster) as ImageView
        textRatingContent = findViewById<View>(R.id.textRatingCaption) as TextView
        textDateRelease = findViewById<View>(R.id.textDateRelease) as TextView
        ratingBar = findViewById<View>(R.id.ratingBar) as RatingBar
        collapsingToolbarLayout =
            findViewById<View>(R.id.collapsing_toolbar) as CollapsingToolbarLayout
        recyclerViewReviews = findViewById<View>(R.id.recyclerViewReviews) as RecyclerView
        textInfoCaption = findViewById<View>(R.id.text_caption) as TextView
        progressBar = findViewById<View>(R.id.progress_bar) as ProgressBar
        imagePlayIcon = findViewById<View>(R.id.imageViewPlayIcon) as ImageView
        buttonFavorite = findViewById<View>(R.id.buttonFavorite) as LikeButton
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recyclerViewReviews)
        reviewsAdapter = ReviewsAdapter(reviewResultsItems, this)
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        linearLayoutManager.isAutoMeasureEnabled = true
        recyclerViewReviews!!.isNestedScrollingEnabled = false
        recyclerViewReviews!!.setHasFixedSize(false)
        recyclerViewReviews!!.layoutManager = linearLayoutManager
        recyclerViewReviews!!.adapter = reviewsAdapter
        progressDialog = ProgressDialog(this)
        builder = AlertDialog.Builder(this)
        builder!!.setPositiveButton(getString(R.string.button_ok)) { dialog, which -> finish() }
        //set actionbar
        setSupportActionBar(findViewById<View>(R.id.toolbar) as Toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle(null)
        //clear content
        clearContent()

        //set api caller
        val apiService = ApiClient.client!!.create(ApiInterface::class.java)
        val intent = intent
        if (intent.hasExtra(ConstantData.MOVIE_ID_KEY)) {
            movieId = intent.getStringExtra(ConstantData.MOVIE_ID_KEY)!!.toInt()
            //check favorite flag
            checkFavFlag()
            getDataDetail(apiService, movieId!!, savedInstanceState)
            getReviews(apiService, movieId!!, savedInstanceState)
            getTrailer(apiService, movieId!!, savedInstanceState)
        }
        imagePlayIcon!!.setOnClickListener {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(YOUTUBE_ENDPOINT + idTrailer)
            )
            startActivity(browserIntent)
        }
        buttonFavorite!!.setOnLikeListener(object : OnLikeListener {
            override fun liked(likeButton: LikeButton) {
                val contentValues = ContentValues()
                contentValues.put(FavoriteEntry.COLUMN_MOVIE_ID, movieId)
                contentValues.put(FavoriteEntry.COLUMN_TITLE, modelMovieDetail.originalTitle)
                contentValues.put(FavoriteEntry.COLUMN_THUMBNAIL, modelMovieDetail.posterPath)
                val cursor = contentResolver
                    .query(
                        FavoriteEntry.CONTENT_URI, null, FavoriteEntry.COLUMN_MOVIE_ID + "=" +
                                movieId.toString(), null, null
                    )
                if (cursor != null) {
                    if (cursor.count == 0) {
                        contentResolver.insert(FavoriteEntry.CONTENT_URI, contentValues)
                    } else {
                        contentResolver.update(
                            FavoriteEntry.CONTENT_URI,
                            contentValues,
                            FavoriteEntry.COLUMN_MOVIE_ID + "=" +
                                    movieId.toString(),
                            null
                        )
                    }
                }
            }

            override fun unLiked(likeButton: LikeButton) {
                contentResolver.delete(
                    FavoriteEntry.CONTENT_URI, FavoriteEntry.COLUMN_MOVIE_ID + "=" +
                            movieId.toString(), null
                )
            }
        })
    }

    private fun checkFavFlag() {
        val cursor = contentResolver
            .query(
                FavoriteEntry.CONTENT_URI, null, FavoriteEntry.COLUMN_MOVIE_ID + "=" +
                        movieId.toString(), null, null
            )
        if (cursor != null) {
            if (cursor.count > 0) {
                buttonFavorite!!.isLiked = true
            } else {
                buttonFavorite!!.isLiked = false
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(KEY_SAVED_INSTANCE_STATE, modelMovieDetail)
        outState.putParcelableArrayList(
            KEY_SAVED_REVIEW_INSTANCE_STATE,
            reviewResultsItems as ArrayList<out Parcelable?>
        )
        outState.putString(KEY_SAVED_TRAILER_INSTANCE_STATE, idTrailer)
        super.onSaveInstanceState(outState)
    }

    private fun clearContent() {
        textTitle?.text = null
        textDateRelease?.text = null
        textRatingContent?.text = null
        textSynopsisCaption?.text = null
        textSynopsisContent?.text = null
        ratingBar!!.rating = 0f
        imageBackDrop!!.setImageResource(0)
        imagePoster!!.setImageResource(0)
    }

    private fun getDataDetail(apiService: ApiInterface, movieId: Int, savedInstanceState: Bundle?) {
        Log.d("Movie Id", movieId.toString())
        val callMovieDetail = apiService.getMovieDetails(movieId, BuildConfig.API_KEY)
        if (NetworkUtil.isOnline(this)) {

            //null and check saved instance state
            if (savedInstanceState == null || !savedInstanceState.containsKey(
                    KEY_SAVED_INSTANCE_STATE
                )
            ) {
                showProgressBar()
                callMovieDetail!!.clone().enqueue(object : Callback<MovieDetail?> {
                    override fun onResponse(
                        call: Call<MovieDetail?>,
                        response: Response<MovieDetail?>
                    ) {
                        hideProgressBar()
                        Log.d("Response Detail", response.body().toString())
                        val model = response.body()
                        if (model != null) {
                            mappingNecessaryData(model)
                            showData(modelMovieDetail)
                        } else {
                            showNoDataCaption()
                        }
                    }

                    override fun onFailure(call: Call<MovieDetail?>, t: Throwable) {
                        hideProgressBar()
                        showFailedCaption()
                    }
                })
                callMovieDetail!!.clone().enqueue(object : Callback<MovieDetail?> {
                    override fun onResponse(
                        call: Call<MovieDetail?>,
                        response: Response<MovieDetail?>
                    ) {
                        hideProgressBar()
                        Log.d("Response Detail", response.body().toString())
                        val model = response.body()
                        if (model != null) {
                            mappingNecessaryData(model)
                            showData(modelMovieDetail)
                        } else {
                            showNoDataCaption()
                        }
                    }

                    override fun onFailure(call: Call<MovieDetail?>, t: Throwable) {
                        hideProgressBar()
                        showFailedCaption()
                    }
                })
            } else {
                hideProgressBar()
                modelMovieDetail =
                    savedInstanceState.getParcelable(KEY_SAVED_INSTANCE_STATE) ?: ModelMovieDetail()
                showData(modelMovieDetail)
            }
        } else {
            showNoConnectivityCaption()
        }
    }

    private fun getReviews(apiService: ApiInterface, movieId: Int, savedInstanceState: Bundle?) {
        val callReviews = apiService.getReviews(movieId, BuildConfig.API_KEY)
        if (NetworkUtil.isOnline(this)) {

            //null and check saved instance state
            if (savedInstanceState == null || !savedInstanceState.containsKey(
                    KEY_SAVED_REVIEW_INSTANCE_STATE
                )
            ) {
                showProgressReviewBar()
                callReviews!!.clone().enqueue(object : Callback<Reviews?> {
                    override fun onResponse(call: Call<Reviews?>, response: Response<Reviews?>) {
                        hideProgressReviewBar()
                        Log.d("Response Detail", response.body().toString())
                        val model = response.body()
                        if (model != null) {
                            hideNoReviewsCaption()
                            showReviewsData(model)
                        } else {
                            showNoReviewsCaption()
                        }
                    }

                    override fun onFailure(call: Call<Reviews?>, t: Throwable) {
                        hideProgressReviewBar()
                        showFailedGettingReviewsCaption()
                    }
                })
            } else {
                hideProgressReviewBar()
                val parcelableData: MutableList<ReviewResultsItem> = savedInstanceState
                    .getParcelableArrayList(KEY_SAVED_REVIEW_INSTANCE_STATE)!!
                reviewResultsItems.addAll(parcelableData)
                if (reviewResultsItems.size == 0) {
                    showNoReviewsCaption()
                    clearReviewListData()
                } else {
                    hideNoReviewsCaption()
                    reviewsAdapter!!.notifyDataSetChanged()
                }
            }
        } else {
            showFailedGettingReviewsCaption()
        }
    }

    private fun getTrailer(apiService: ApiInterface, movieId: Int, savedInstanceState: Bundle?) {
        Log.d("Movie Id", movieId.toString())
        val callTrailer = apiService.getTrailer(movieId, BuildConfig.API_KEY)
        if (NetworkUtil.isOnline(this)) {
            if (savedInstanceState == null || !savedInstanceState.containsKey(
                    KEY_SAVED_TRAILER_INSTANCE_STATE
                )
            ) {
                callTrailer!!.clone().enqueue(object : Callback<Trailer?> {
                    override fun onResponse(call: Call<Trailer?>, response: Response<Trailer?>) {
                        Log.d("Response Detail", response.body().toString())
                        val model = response.body()
                        model?.let { mapTrailerData(it) }
                    }

                    override fun onFailure(call: Call<Trailer?>, t: Throwable) {}
                })
            } else {
                idTrailer = savedInstanceState.getString(KEY_SAVED_TRAILER_INSTANCE_STATE)
            }
        }
    }

    private fun mapTrailerData(model: Trailer) {
        if (model.results.size === 0) {
            hidePlayIcon()
        } else {
            showPlayIcon()
            idTrailer = model.results.get(0).key
        }
    }

    private fun showReviewsData(model: Reviews) {
        if (model.totalResults == 0) {
            showNoReviewsCaption()
        } else {
            reviewResultsItems.addAll(model.results)
            reviewsAdapter!!.notifyDataSetChanged()
        }
    }

    private fun clearReviewListData() {
        reviewResultsItems.clear()
        reviewsAdapter!!.notifyDataSetChanged()
    }

    private fun showPlayIcon() {
        imagePlayIcon!!.visibility = View.VISIBLE
    }

    private fun hidePlayIcon() {
        imagePlayIcon!!.visibility = View.GONE
    }

    private fun showData(modelMovieDetail: ModelMovieDetail) {
        collapsingToolbarLayout!!.title = modelMovieDetail.originalTitle
        collapsingToolbarLayout!!.setExpandedTitleColor(resources.getColor(R.color.transparent))
        textTitle!!.text = modelMovieDetail.originalTitle
        textDateRelease!!.text =
            DateFormatter.getYear(modelMovieDetail.releaseDate)
        textRatingContent!!.text =
            getString(R.string.rating_placeholder_caption, modelMovieDetail.voteAverage)
        textSynopsisCaption!!.text = getString(R.string.synopsis_caption)
        textSynopsisContent!!.text = modelMovieDetail.overview
        val ratingValue: Double = modelMovieDetail.voteAverage / 2
        ratingBar!!.rating = ratingValue.toFloat()
        val baseUrl = "http://image.tmdb.org/t/p/"
        val imageSize = "w185"
        val backdropPath: String = modelMovieDetail.backdropPath
        val posterPath: String = modelMovieDetail.posterPath
        val backdropUrl = baseUrl + imageSize + backdropPath
        val posterUrl = baseUrl + imageSize + posterPath
        Picasso.with(applicationContext).load(backdropUrl).into(imageBackDrop)
        Log.d("Backdrop Url", backdropUrl)
        Picasso.with(applicationContext).load(posterUrl).into(imagePoster)
        Log.d("Poster Url", posterUrl)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun showProgressBar() {
        progressDialog!!.setMessage(getString(R.string.loading_caption))
        progressDialog!!.show()
    }

    private fun hideProgressBar() {
        if (progressDialog!!.isShowing) {
            progressDialog!!.dismiss()
        }
    }

    private fun showNoConnectivityCaption() {
        builder!!.setMessage(getString(R.string.no_connectivity_caption))
        val alert = builder!!.create()
        alert.show()
    }

    private fun showNoDataCaption() {
        builder!!.setMessage(getString(R.string.no_data_caption))
        val alert = builder!!.create()
        alert.show()
    }

    private fun showFailedCaption() {
        builder!!.setMessage(getString(R.string.failed_getting_data_caption))
        val alert = builder!!.create()
        alert.show()
    }

    private fun showNoReviewsCaption() {
        textInfoCaption!!.text = getString(R.string.no_data_reviews_caption)
        textInfoCaption!!.visibility = View.VISIBLE
    }

    private fun showFailedGettingReviewsCaption() {
        textInfoCaption!!.text = getString(R.string.failed_getting_data_caption)
        textInfoCaption!!.visibility = View.VISIBLE
    }

    private fun hideNoReviewsCaption() {
        textInfoCaption!!.visibility = View.GONE
    }

    private fun showProgressReviewBar() {
        progressBar!!.visibility = View.VISIBLE
    }

    private fun hideProgressReviewBar() {
        progressBar!!.visibility = View.GONE
    }

    private fun mappingNecessaryData(movieDetail: MovieDetail) {
        modelMovieDetail.originalTitle = movieDetail.originalTitle
        modelMovieDetail.backdropPath = movieDetail.backdropPath
        modelMovieDetail.posterPath = movieDetail.posterPath
        modelMovieDetail.overview = movieDetail.overview
        modelMovieDetail.voteAverage = movieDetail.voteAverage
        modelMovieDetail.releaseDate = movieDetail.releaseDate
    }

    override fun onClick(data: String?) {
        //click action for recyclerview reviews

//    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(data));
//    startActivity(browserIntent);
    }

    companion object {
        private const val KEY_SAVED_INSTANCE_STATE = "movie_detail_key"
        private const val KEY_SAVED_REVIEW_INSTANCE_STATE = "movie_reviews_key"
        private const val KEY_SAVED_TRAILER_INSTANCE_STATE = "movie_trailer_key"
        private const val YOUTUBE_ENDPOINT = "https://www.youtube.com/watch?v="
    }
}