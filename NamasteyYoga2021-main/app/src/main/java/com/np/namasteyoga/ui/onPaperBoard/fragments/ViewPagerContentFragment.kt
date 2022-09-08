package com.np.namasteyoga.ui.onPaperBoard.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.np.namasteyoga.R
import com.np.namasteyoga.ui.onPaperBoard.adapter.OnPaperModel
import kotlinx.android.synthetic.main.view_pager_content.*

class ViewPagerContentFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.view_pager_content, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.takeIf { it.containsKey(DATA_SHARE) }?.apply {
            (getParcelable(DATA_SHARE) as OnPaperModel?)?.run {
                image.setImageResource(img)
                heading.text = title
                txtMsg.text = msg
            }
        }

    }




    companion object{
        private const val DATA_SHARE = "data_share"
        fun newInstance(onPaperModel: OnPaperModel):ViewPagerContentFragment{
            val instance = ViewPagerContentFragment()
            instance.arguments = Bundle().apply {
                putParcelable(DATA_SHARE,onPaperModel)

            }
           return instance
        }
    }
}
