package com.yusufemre.socialhelper

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.widget.Toast

import com.surveymonkey.surveymonkeyandroidsdk.SMFeedbackFragment
import com.surveymonkey.surveymonkeyandroidsdk.SMFeedbackFragmentListener
import com.surveymonkey.surveymonkeyandroidsdk.SurveyMonkey
import com.surveymonkey.surveymonkeyandroidsdk.utils.SMError

import org.json.JSONObject


class SimpleFragmentActivity : FragmentActivity(), SMFeedbackFragmentListener {
    private var mCollectorHash: String? = null
    private var mThanksToast: Toast? = null
    private var mErrorToast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = intent
        mCollectorHash = intent.getStringExtra(COLLECTOR_HASH)
        if (savedInstanceState == null) {
            //This is how you can add the SMFeedbackFragment to your activity
            supportFragmentManager.beginTransaction().add(android.R.id.content, SurveyMonkey.newSMFeedbackFragmentInstance(mCollectorHash), SMFeedbackFragment.TAG).commit()
        }

        //Create Toasts to Display to User
        mErrorToast = Toast.makeText(applicationContext, getString(R.string.error_prompt), Toast.LENGTH_LONG)
        mThanksToast = Toast.makeText(this, getString(R.string.thanks_prompt), Toast.LENGTH_LONG)
    }


    override fun onFetchRespondentSuccess(respondent: JSONObject) {
        mThanksToast!!.show()
        finish()
    }

    override fun onFetchRespondentFailure(e: SMError) {
        mErrorToast!!.show()
        Log.d("SM-ERROR", e.getDescription())
        finish()
    }

    companion object {


        val COLLECTOR_HASH = "collectorHash"

        fun startActivity(context: Activity, collectorHash: String?) {
            val intent = Intent(context, SimpleFragmentActivity::class.java)
            if (collectorHash != null) {
                intent.putExtra(COLLECTOR_HASH, collectorHash)
            }
            context.startActivity(intent)
        }
    }
}
