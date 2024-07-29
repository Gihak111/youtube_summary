import sys
import urllib.parse
from youtube_transcript_api import YouTubeTranscriptApi
from transformers import pipeline
import logging
import warnings

# Suppress TensorFlow warnings
logging.getLogger("tensorflow").setLevel(logging.ERROR)
warnings.filterwarnings("ignore", category=UserWarning, module='tensorflow')

def get_video_id(url):
    url = urllib.parse.unquote(url)

    if 'youtube.com' in url:
        if 'v=' in url:
            return url.split('v=')[1].split('&')[0]  # Get video ID from the URL
    elif 'youtu.be' in url:
        return url.split('/')[-1]
    else:
        raise ValueError("Invalid YouTube URL")

def summarize_text(text):
    summarizer = pipeline('summarization', model="facebook/bart-large-cnn")
    summary = summarizer(text, max_length=130, min_length=30, do_sample=False)
    return summary[0]['summary_text']

def main(url):
    try:
        video_id = get_video_id(url)
        if not isinstance(video_id, str):
            raise ValueError("Extracted video_id is not a string")

        transcript = YouTubeTranscriptApi.get_transcript(video_id)
        text = " ".join([item['text'] for item in transcript])

        summary = summarize_text(text)

        # Print only the summary to stdout
        print(summary)
    except Exception as e:
        # Print error message to stderr
        print(f"Error: {str(e)}", file=sys.stderr)

if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("Usage: python youtube_summary.py <URL>")
        sys.exit(1)

    url = sys.argv[1]
    main(url)
