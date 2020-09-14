package com.cris.rest;
import java.util.logging.Level;
import java.util.logging.Logger;

import pt.ptcris.handlers.ProgressHandler;

/**
 * A simple implementation of the progress handler to log the execution of
 * PTCRISync procedures.
 */
public class Tester implements ProgressHandler {
	private static Logger logger = Logger.getLogger(Tester.class.getName());
	private int max;
	private int step;
	
	/** {@inheritDoc} */
	@Override
	public void setProgress(int progress) {
		logger.fine("Current progress: " + progress + "%");
	}

	/** {@inheritDoc} */
	@Override
	public void setCurrentStatus(String message) {
		logger.fine("Task: " + message);
	}
	
	/** {@inheritDoc} */
	@Override
	public void setCurrentStatus(String message, int n) {
		max = n;
		step = 0;
		logger.fine("Task: " + message + " (" + n + ")");
		progress();
	}

	/** {@inheritDoc} */
	@Override
	public void sendError(String message) {
		logger.log(Level.SEVERE, "ERROR: " + message);
	}

	/** {@inheritDoc} */
	@Override
	public void done() {
		logger.fine("Done.");
	}
	
	/** {@inheritDoc} */
	@Override
	public void step() {
		step++;
		progress();
	}
	
	/** {@inheritDoc} */
	@Override
	public void step(int s) {
		step+=s;
		progress();
	}

	private void progress() {
		if (max > 0)
			setProgress((step*100)/max);
	}
}