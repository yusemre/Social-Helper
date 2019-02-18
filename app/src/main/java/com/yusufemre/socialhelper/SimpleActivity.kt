package com.yusufemre.socialhelper


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast

import com.surveymonkey.surveymonkeyandroidsdk.SurveyMonkey
import com.surveymonkey.surveymonkeyandroidsdk.utils.SMError

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class SimpleActivity : Activity() {
    // Initialize the SurveyMonkey SDK like so
    private val s = SurveyMonkey()

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple)
        //Here we're setting up the SurveyMonkey Intercept Dialog -- the user will be prompted to take the survey 3 days after app install.
        // Once prompted, the user will be reminded to take the survey again in 3 weeks if they decline or 3 months if they consent to take the survey.
        // The onStart method can be overloaded so that you can supply your own prompts and intervals -- for more information, see our documentation on Github.
        s.onStart(this, SAMPLE_APP, SM_REQUEST_CODE, SURVEY_HASH)
    }

    override fun onStart() {
        super.onStart()

    }

    fun takeSurvey(view: View) {
        //This is how you display a survey for the user to take
        // Remember: you must supply the parent activity (e.g. this), your own request code (to differentiate from other activities), and the collector hash of the SDK collector you've created at SurveyMonkey.com
        s.startSMFeedbackActivityForResult(this, SM_REQUEST_CODE, SURVEY_HASH)
    }

    fun takeSurveyFragment(view: View) {
        //This is how you display a survey for the user to take
        // Remember: you must supply the parent activity (e.g. this), your own request code (to differentiate from other activities), and the collector hash of the SDK collector you've created at SurveyMonkey.com
        SimpleFragmentActivity.startActivity(this, SURVEY_HASH)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent) {
        //This is where you consume the respondent data returned by the SurveyMonkey Mobile Feedback SDK
        //In this example, we deserialize the user's response, check to see if they gave our app 4 or 5 stars, and then provide visual prompts to the user based on their response
        super.onActivityResult(requestCode, resultCode, intent)
        if (resultCode == Activity.RESULT_OK) {
            var isPromoter = false
            try {
                val respondent = intent.getStringExtra(SM_RESPONDENT)
                Log.d("SM", respondent)
                val surveyResponse = JSONObject(respondent)
                val responsesList = surveyResponse.getJSONArray(RESPONSES)
                var response: JSONObject
                var answers: JSONArray
                var currentAnswer: JSONObject
                for (i in 0 until responsesList.length()) {
                    response = responsesList.getJSONObject(i)
                    if (response.getString(QUESTION_ID) == FEEDBACK_QUESTION_ID) {
                        answers = response.getJSONArray(ANSWERS)
                        for (j in 0 until answers.length()) {
                            currentAnswer = answers.getJSONObject(j)
                            if (currentAnswer.getString(ROW_ID) == FEEDBACK_FIVE_STARS_ROW_ID || currentAnswer.getString(ROW_ID) == FEEDBACK_POSITIVE_ROW_ID_2) {
                                isPromoter = true
                                break
                            }
                        }
                        if (isPromoter) {
                            break
                        }
                    }
                }
            } catch (e: JSONException) {
                Log.getStackTraceString(e)
            }

            if (isPromoter) {
                val t = Toast.makeText(this, getString(R.string.promoter_prompt), Toast.LENGTH_LONG)
                t.show()
            } else {
                val t = Toast.makeText(this, getString(R.string.detractor_prompt), Toast.LENGTH_LONG)
                t.show()
            }
        } else {
            val t = Toast.makeText(this, getString(R.string.error_prompt), Toast.LENGTH_LONG)
            t.show()
            val e = intent.getSerializableExtra(SM_ERROR) as SMError
            Log.d("SM-ERROR", e.getDescription())
        }
    }


    override fun onStop() {
        super.onStop()
    }

    companion object {

        val SM_REQUEST_CODE = 0
        val SM_RESPONDENT = "smRespondent"
        val SM_ERROR = "smError"
        val RESPONSES = "responses"
        val QUESTION_ID = "question_id"
        val FEEDBACK_QUESTION_ID = "813797519"
        val ANSWERS = "answers"
        val ROW_ID = "row_id"
        val FEEDBACK_FIVE_STARS_ROW_ID = "9082377273"
        val FEEDBACK_POSITIVE_ROW_ID_2 = "9082377274"
        val SAMPLE_APP = "Sample App"
        val SURVEY_HASH = "7MPZJTC"
    }
}
