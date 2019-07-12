package ir

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import ir.sharif.taxifinder.MainActivity
import ir.sharif.taxifinder.R

/**
 * Implementation of App Widget functionality.
 */
class TaxiAppWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    companion object {

        internal fun updateAppWidget(
            context: Context, appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {

            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

            val remoteViews = RemoteViews(context.packageName, R.layout.taxi_app_widget)

            remoteViews.setOnClickPendingIntent(R.id.textView, pendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, remoteViews)


//            val widgetText = context.getString(R.string.appwidget_text)
//            // Construct the RemoteViews object
//            val views = RemoteViews(context.packageName, R.layout.taxi_app_widget)
//            views.setTextViewText(R.id.appwidget_text, widgetText)
//
//            // Instruct the widget manager to update the widget
//            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}

