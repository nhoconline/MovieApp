//package trantuan.demo.movieapp.ui.upcoming
//
//import android.R
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ProgressBar
//import android.widget.TextView
//import android.widget.Toast
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import trantuan.demo.movieapp.data.model.movieUpcoming.ResultUpcoming
//
//
//class DataAdapter : RecyclerView.Adapter {
//    private val VIEW_ITEM = 1
//    private val VIEW_PROG = 0
//
//    private var studentList: List<ResultUpcoming>? = null
//
//    // The minimum amount of items to have below your current scroll position
//    // before loading more.
//    private val visibleThreshold = 5
//    private var lastVisibleItem = 0
//    private var totalItemCount: Int = 0
//    private var loading = false
//    private var onLoadMoreListener: OnLoadMoreListener? = null
//
//    fun DataAdapter(students: List<ResultUpcoming?>, recyclerView: RecyclerView) {
//        studentList = students
//        if (recyclerView.layoutManager is LinearLayoutManager) {
//            val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
//            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//                override fun onScrolled(
//                    recyclerView: RecyclerView,
//                    dx: Int, dy: Int
//                ) {
//                    super.onScrolled(recyclerView, dx, dy)
//                    totalItemCount = linearLayoutManager!!.itemCount
//                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition()
//                    if (!loading
//                        && totalItemCount <= lastVisibleItem + visibleThreshold
//                    ) {
//                        // End has been reached
//                        // Do something
//                        if (onLoadMoreListener != null) {
//                            onLoadMoreListener!!.onLoadMore()
//                        }
//                        loading = true
//                    }
//                }
//            })
//        }
//    }
//
//    fun getItemViewType(position: Int): Int {
//        return if (studentList!![position] != null) VIEW_ITEM else VIEW_PROG
//    }
//
//    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
//        val vh: RecyclerView.ViewHolder
//        if (viewType == VIEW_ITEM) {
//            val v: View = LayoutInflater.from(parent.context).inflate(
//                R.layout.list_up_movie, parent, false
//            )
//            vh = StudentViewHolder(v)
//        } else {
//            val v: View = LayoutInflater.from(parent.context).inflate(
//                R.layout.progressbar_item, parent, false
//            )
//            vh = ProgressViewHolder(v)
//        }
//        return vh
//    }
//
//    fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        if (holder is StudentViewHolder) {
//            val singleStudent: ResultUpcoming = studentList!![position] as ResultUpcoming
//            holder.tvName.setText(singleStudent.getName())
//            holder.tvEmailId.setText(singleStudent.getEmailId())
//            holder.student = singleStudent
//        } else {
//            (holder as ProgressViewHolder).progressBar.isIndeterminate = true
//        }
//    }
//
//    fun setLoaded() {
//        loading = false
//    }
//
//    fun getItemCount(): Int {
//        return studentList!!.size
//    }
//
//    fun setOnLoadMoreListener(onLoadMoreListener: OnLoadMoreListener?) {
//        this.onLoadMoreListener = onLoadMoreListener
//    }
//
//    //
//    class StudentViewHolder(v: View) : RecyclerView.ViewHolder(v) {
//        var tvName: TextView
//        var tvEmailId: TextView
//        var student: ResultUpcoming? = null
//
//        init {
//            tvName = v.findViewById(R.id.tvName)
//            tvEmailId = v.findViewById(R.id.tvEmailId)
//
//            v.setOnClickListener(object : OnClickListener() {
//                fun onClick(v: View) {
//                    Toast.makeText(
//                        v.context,
//                        """OnClick :${student.getName().toString()}
//                            |${student.getEmailId()}""".trimMargin(),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            })
//        }
//    }
//
//    class ProgressViewHolder(v: View) : RecyclerView.ViewHolder(v) {
//        var progressBar: ProgressBar = v.findViewById(R.id.progress)
//
//    }
//
//}